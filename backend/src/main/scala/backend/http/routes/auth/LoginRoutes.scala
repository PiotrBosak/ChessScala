package backend.http.routes.auth

import backend.domain._
import backend.domain.auth._
import backend.ext.http4s.refined._
import backend.algebras.AuthAlg
import backend.http.auth.users.User
import cats.MonadThrow
import cats.syntax.all._
import org.http4s._

import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class LoginRoutes[F[_] : MonadThrow: JsonDecoder](
                                          authAlg: AuthAlg[F]
                                          ) extends Http4sDsl[F] {

  private [routes] val prefixPath = "/"

  private val authedRoutes : HttpRoutes[F] = HttpRoutes.of {
    case req @ POST  -> Root / "login" =>
      req.decodeR[LoginUser] { user =>
        authAlg
          .login(user.username.toDomain,user.password.toDomain)
          .flatMap(Ok(_))
          .recoverWith {
            case UserNotFound(_) | InvalidPassword(_) => Forbidden()
          }

      }
  }

}
