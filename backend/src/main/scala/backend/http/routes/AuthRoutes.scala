package backend.http.routes

import backend.algebras.AuthAlg
import backend.http.auth.users.User
import cats.Monad
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.dsl.Http4sDsl

final case class AuthRoutes[F[_] : Monad](
                                           authAlg: AuthAlg[F]
                                         ) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: AuthedRoutes[User, F] = AuthedRoutes.of {
    case GET -> Root as user =>
      Ok(s"Welcome, ${user.name}")
  }
}
