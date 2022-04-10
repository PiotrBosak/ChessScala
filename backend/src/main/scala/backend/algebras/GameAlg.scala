package backend.algebras

import backend.domain.auth.UserId
import backend.domain.game.{DrawProposal, DrawProposalAnswer, DrawProposalAnswerResult, ForfeitResult, GameId,  Move, MoveResult,  PvPGame}
import cats.{Applicative, MonadThrow}
import cats.effect.Temporal
import cats.effect.kernel.Resource
import chesslogic.board.Board
import io.circe.syntax.*
import cats.syntax.all.*
import backend.domain.RedisEncodeExt.asRedis
import backend.domain.RedisEncode._
import chesslogic.game.{BlackPlayer, Player, WhitePlayer}
import dev.profunktor.redis4cats.RedisCommands
import io.circe.Json
import skunk.Session
import MoveResult.*

trait GameAlg[F[_]] {

  def makeMove(userId: UserId, gameId: GameId, move: Move): F[MoveResult]

  def proposeDraw(userId: UserId, gameId: GameId): F[DrawProposal]

  def answerDrawProposal(userId: UserId, gameId: GameId, drawProposalAnswer: DrawProposalAnswer): F[DrawProposalAnswerResult]

  def forfeit(userId: UserId, gameId: GameId): F[ForfeitResult]

}

object GameAlg {
  def make[F[_] : Temporal, Logger](postgres: Resource[F, Session[F]],
                                    redis: RedisCommands[F, String, String],
                                   ): GameAlg[F] = new GameAlg[F] {
    override def makeMove(userId: UserId, gameId: GameId, move: Move): F[MoveResult] = {
      redis
        .get(gameId.toString)
        .flatMap {
          case Some(game) =>
            Json.fromString(game).as[PvPGame].liftTo[F].flatMap(game => {
              val playerF: F[Player] = {
                if (game.whitePlayer == userId) Applicative[F].pure(WhitePlayer)
                else if (game.blackPlayer == userId) Applicative[F].pure(BlackPlayer)
                //todo fix it somehow
                else MonadThrow[F].raiseError(new Throwable("player plumbula"))
              }
              playerF.flatMap { player =>
                game
                  .simpleGame
                  .makeMove(player, move.from, move.to) match {
                  case Some(newGame) =>
                    redis.set(gameId.toString, game.copy(simpleGame = newGame).asRedis) >>
                      Applicative[F].pure[MoveResult](MoveSuccessful(newGame.currentBoard))
                  case None => Applicative[F].pure(IllegalMove)
                }
              }
            }
            )

          case None => Applicative[F].pure(GameNotFound)
        }
    }

    //    private def

    override def proposeDraw(userId: UserId, gameId: GameId): F[DrawProposal] = MonadThrow[F].raiseError(new Exception)

    override def answerDrawProposal(userId: UserId, gameId: GameId, drawProposalAnswer: DrawProposalAnswer): F[DrawProposalAnswerResult] = MonadThrow[F].raiseError(new Exception)

    override def forfeit(userId: UserId, gameId: GameId): F[ForfeitResult] = MonadThrow[F].raiseError(new Exception)
  }
}
