package gateway.http.routes.auth

import gateway.algebras.AuthAlg
import gateway.domain.auth.{ CreateUser, UserNameInUse }
import lib.domain.auth.{ UserName }
import gateway.ext.http4s.refined.RefinedRequestDecoder
import gateway.http.routes.auth.UserRoutes.RegistrationResponse
import cats.MonadThrow
import gateway.domain.jwt._
import cats.syntax.all.*
import io.circe.{ Encoder, Json, JsonObject }
import io.circe.Json.{ JObject, JString }
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.JsonDecoder
import org.http4s.Status.*
import org.http4s.dsl.Http4sDsl
import lib.domain.auth.*
import gateway.domain.auth.UserNameParamExtensions.toDomain as userToDomain
import gateway.domain.auth.PasswordParamExtensions.toDomain as passwordToDomain
import gateway.domain.auth.EmailParamExtensions.toDomain as emailToDomain
import org.http4s.server.Router

final case class UserRoutes[F[_]: JsonDecoder: MonadThrow](
    auth: AuthAlg[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "users" =>
    req
      .decodeR[CreateUser] { user =>
        auth
          .newUser(user.username.userToDomain, user.email.emailToDomain, user.password.passwordToDomain)
          .flatMap(jwt => Created(RegistrationResponse(jwt, user.username.userToDomain)))
          .recoverWith { case UserNameInUse(u) =>
            Conflict(u.show)
          }
      }

  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}

object UserRoutes {
  final case class RegistrationResponse(
      token: JwtToken,
      username: UserName
  )

  implicit val encoder: Encoder[RegistrationResponse] = Encoder.instance { rr =>
    import io.circe.syntax.*
    Json.fromFields(
      List(
        "user" -> Json.fromFields(
          List(
            "username" -> rr.username.value.asJson,
            "token"    -> rr.token.value.asJson
          )
        )
      )
    )

  }
}
