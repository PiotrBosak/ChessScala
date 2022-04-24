package backend.http.routes.auth

import backend.algebras.AuthAlg
import backend.domain.auth.{CreateUser, UserName, UserNameInUse}
import backend.ext.http4s.refined.RefinedRequestDecoder
import backend.http.routes.auth.UserRoutes.RegistrationResponse
import cats.MonadThrow
import cats.syntax.all._
import dev.profunktor.auth.jwt.JwtToken
import io.circe.{Encoder, Json}
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class UserRoutes[F[_] : JsonDecoder : MonadThrow](
                                                              auth: AuthAlg[F]
                                                            ) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case req@POST -> Root / "users" =>
      req.decodeR[CreateUser] { user =>
        auth
          .newUser(user.username.toDomain, user.email.toDomain, user.password.toDomain)
          .flatMap(jwt => Created(RegistrationResponse(jwt, user.username.toDomain)))
          .recoverWith {
            case UserNameInUse(u) => Conflict(u.show)
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
    import io.circe.syntax._
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
