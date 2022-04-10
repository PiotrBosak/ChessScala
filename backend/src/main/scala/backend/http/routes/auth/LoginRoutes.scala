package backend.http.routes.auth

import backend.algebras.AuthAlg
import backend.domain.auth.{ InvalidPassword, LoginUser, UserNotFound }
import backend.ext.http4s.refined.RefinedRequestDecoder
import cats.MonadThrow
import cats.syntax.all.*
import org.http4s.*
import backend.domain.auth.UserNameParamExtensions.toDomain as userToDomain
import backend.domain.auth.PasswordParamExtensions.toDomain as passwordToDomain
import backend.domain.auth.EmailParamExtensions.toDomain as emailToDomain
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class LoginRoutes[F[_]: JsonDecoder: MonadThrow](
    auth: AuthAlg[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "login" =>
    req.decodeR[LoginUser] { user =>
      auth
        .login(user.username.userToDomain, user.password.passwordToDomain)
        .flatMap(Ok(_))
        .recoverWith { case UserNotFound(_) | InvalidPassword(_) =>
          Forbidden()
        }
    }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
