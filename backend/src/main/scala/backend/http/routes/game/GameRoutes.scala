package backend.http.routes.game

import backend.algebras.GameAlg
import backend.domain.game.{GameId, Move}
import backend.http.auth.users.CommonUser
import backend.http.routes.game.GameRoutes.MakeMoveRequest
import cats.MonadThrow
import derevo.circe.magnolia.decoder
import derevo.derive
import cats.syntax.all._
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server._

final case class GameRoutes[F[_] : MonadThrow : JsonDecoder](gameAlg: GameAlg[F]) extends Http4sDsl[F] {

  private[game] val prefixPath = "/gameSearch"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of[CommonUser, F] {
    case ar@POST -> Root / "makeMove" as user =>
      ar.req
        .asJsonDecode[MakeMoveRequest]
        .flatMap { move =>
          gameAlg.makeMove(user.value.id, move.gameId, move.move)
        }
        .flatMap(Ok(_))

  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
    prefixPath -> authMiddleware(httpRoutes)
  )

}

object GameRoutes {
  @derive(decoder)
  final case class MakeMoveRequest(
                                    gameId: GameId,
                                    move: Move
                                  )
}
