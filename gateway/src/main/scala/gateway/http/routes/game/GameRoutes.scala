package gateway.http.routes.game

import gateway.algebras.GameAlg
import gateway.domain.game.{ GameId, Move }
import gateway.http.auth.users.CommonUser
import gateway.http.routes.game.GameRoutes.MakeMoveRequest
import cats.MonadThrow
import cats.syntax.all.*
import io.circe.Codec
import org.http4s.*
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.*
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.*

final case class GameRoutes[F[_]: MonadThrow: JsonDecoder]() extends Http4sDsl[F] {
  import MakeMoveRequest._
  private[game] val prefixPath = "/game"

  // private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of[CommonUser, F] {
  //   case ar @ POST -> Root / "makeMove" as user =>
  //     ar.req
  //       .asJsonDecode[MakeMoveRequest]
  //       .flatMap { move =>
  //         gameAlg.makeMove(user.value.id, move.gameId, move.move)
  //       }
  //       .flatMap(Ok(_))
  //
  // }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = ???
}

object GameRoutes {

  final case class MakeMoveRequest(
      gameId: GameId,
      move: Move
  ) derives Codec.AsObject
}
