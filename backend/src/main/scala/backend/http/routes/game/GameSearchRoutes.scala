package backend.http.routes.game

import backend.algebras.GameSearchAlg
import cats.Monad
import cats.syntax.all._
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import cats.syntax.all._
import backend.ext.http4s.refined.RefinedRequestDecoder
import backend.http.auth.users.CommonUser
import org.http4s.Status.Created

final case class GameSearchRoutes[F[_]: Monad](gameSearchAlg: GameSearchAlg[F]) extends Http4sDsl[F]{


  private [game] val prefixPath = "/gameSearch"

  private val httpRoutes: AuthedRoutes[CommonUser,F] = AuthedRoutes.of[CommonUser,F] {

    case POST -> Root / "start" as user =>
      gameSearchAlg
        .startSearch(user.value.id)
        .flatMap(Ok(_))
    case POST -> Root / "poke"  as user=>
      gameSearchAlg
      .poke(user.value.id)
      .flatMap(Ok(_))
    case POST -> Root / "stop" as user=>
      gameSearchAlg
      .stopSearching(user.value.id)
      .flatMap(Ok(_))

  }

  def routes(authMiddleware: AuthMiddleware[F,CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}
