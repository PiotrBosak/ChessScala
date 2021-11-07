package backend.http.routes.auth

import backend.algebras.AuthAlg
import backend.domain.auth.LoginUser
import backend.http.auth.users.CommonUser
import cats.MonadThrow
import dev.profunktor.auth.AuthHeaders
import org.http4s.{AuthedRoutes, HttpRoutes}
import cats.syntax.all._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}

final case class LogoutRoutes[F[_] : MonadThrow : JsonDecoder](
                                                                authAlg: AuthAlg[F]
                                                              ) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val authedRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {
    case ar@POST -> Root / "logout" as user =>
      AuthHeaders
        .getBearerToken(ar.req)
        .traverse_(authAlg.logout(_, user.value.name)) *> NoContent()
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(authedRoutes)
  )
}
