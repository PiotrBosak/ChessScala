package backend.http.routes.auth

import backend.algebras.AuthAlg
import backend.domain.auth.{CreateUser, UserNameInUse}
import backend.ext.http4s.refined.RefinedRequestDecoder
import backend.domain.tokenEncoder
import cats.MonadThrow
import cats.syntax.all._
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
      req
        .decodeR[CreateUser] { user =>
          auth
            .newUser(user.username.toDomain, user.password.toDomain)
            .flatMap(Created(_))
            .recoverWith {
              case UserNameInUse(u) => Conflict(u.show)
            }
        }

  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
