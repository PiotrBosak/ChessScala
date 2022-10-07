package gateway.http.jwt

import gateway.http.jwt.jwt.JwtAuth
import cats.MonadThrow
import cats.data.{ Kleisli, OptionT }
import org.http4s.{ AuthedRoutes, Request }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import gateway.domain.jwt.{ AuthHeaders, JwtToken }
import cats.syntax.all.*
import pdi.jwt.JwtClaim
import pdi.jwt.exceptions.JwtException

object JwtAuthMiddleware {
  def apply[F[_]: MonadThrow, A](
      jwtAuth: JwtAuth,
      authenticate: JwtToken => JwtClaim => F[Option[A]]
  ): AuthMiddleware[F, A] = {
    val dsl = new Http4sDsl[F] {}; import dsl._
    import jwt._

    val onFailure: AuthedRoutes[String, F] =
      Kleisli(req => OptionT.liftF(Forbidden(req.context)))

    val authUser: Kleisli[F, Request[F], Either[String, A]] =
      Kleisli { request =>
        AuthHeaders.getBearerToken(request).fold("Bearer token not found".asLeft[A].pure[F]) { token =>
          jwtDecode[F](token, jwtAuth)
            .flatMap(authenticate(token))
            .map(_.fold("not found".asLeft[A])(_.asRight[String]))
            .recover { case _: JwtException =>
              "Invalid access token".asLeft[A]
            }
        }
      }

    AuthMiddleware(authUser, onFailure)
  }
}
