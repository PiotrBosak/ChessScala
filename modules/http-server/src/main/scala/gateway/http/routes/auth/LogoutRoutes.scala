package gateway.http.routes.auth

import gateway.algebras.AuthAlg
import gateway.http.auth.users.CommonUser
import cats.Monad
import cats.syntax.all.*
import gateway.domain.jwt.AuthHeaders
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{ AuthMiddleware, Router }
import org.http4s.{ AuthedRoutes, HttpRoutes }

final case class LogoutRoutes[F[_]: Monad](
    auth: AuthAlg[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of { case ar @ POST -> Root / "logout" as user =>
    AuthHeaders
      .getBearerToken(ar.req)
      .traverse_(auth.logout(_, user.value.name)) *> NoContent()

  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
