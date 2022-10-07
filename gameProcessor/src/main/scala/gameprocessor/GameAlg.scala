package gameProcessor.algebras

import lib.domain.auth.UserId
import gameProcessor.domain.game.{
  DrawProposal,
  DrawProposalAnswer,
  DrawProposalAnswerResult,
  ForfeitResult,
  Move,
  MoveResult,
}
import lib.domain.game.*
import cats.{ Applicative, MonadThrow }
import cats.effect.Temporal
import cats.effect.kernel.Resource
import io.circe.syntax.*
import cats.syntax.all.*
import lib.effects.RedisEncodeExt.asRedis
import lib.effects.RedisEncode._
import dev.profunktor.redis4cats.RedisCommands
import io.circe.Json
import skunk.Session
import MoveResult.*
import lib.logic.board.Board
import lib.logic.game.{BlackPlayer, Player, WhitePlayer}
import lib.logic.game.PvPGame

trait GameAlg[F[_]] {

  def makeMove(userId: UserId, gameId: GameId, move: Move): F[MoveResult]

  def proposeDraw(userId: UserId, gameId: GameId): F[DrawProposal]

  def answerDrawProposal(
      userId: UserId,
      gameId: GameId,
      drawProposalAnswer: DrawProposalAnswer
  ): F[DrawProposalAnswerResult]

  def forfeit(userId: UserId, gameId: GameId): F[ForfeitResult]

}

object GameAlg {
  def make[F[_]: Temporal, Logger](
      postgres: Resource[F, Session[F]],
      redis: RedisCommands[F, String, String]
  ): GameAlg[F] = new GameAlg[F] {
    override def makeMove(userId: UserId, gameId: GameId, move: Move): F[MoveResult] = {
      redis
        .get(gameId.toString)
        .flatMap {
          case Some(game) =>
            Json
              .fromString(game)
              .as[PvPGame]
              .liftTo[F]
              .flatMap(game => {
                val playerF: F[Player] = {
                  if (game.whitePlayer == userId) Applicative[F].pure(WhitePlayer)
                  else if (game.blackPlayer == userId) Applicative[F].pure(BlackPlayer)
                  //todo fix it somehow
                  else MonadThrow[F].raiseError(new Throwable("player plumbula"))
                }
                playerF.flatMap { player =>
                  game.simpleGame
                    .makeMove(player, move.from, move.to) match {
                    case Some(newGame) =>
                      redis.set(gameId.toString, game.copy(simpleGame = newGame).asRedis) >>
                        Applicative[F].pure[MoveResult](MoveSuccessful(newGame.currentBoard))
                    case None => Applicative[F].pure(IllegalMove)
                  }
                }
              })

          case None => Applicative[F].pure(GameNotFound)
        }
    }

    //    private def

    override def proposeDraw(userId: UserId, gameId: GameId): F[DrawProposal] = MonadThrow[F].raiseError(new Exception)

    override def answerDrawProposal(
        userId: UserId,
        gameId: GameId,
        drawProposalAnswer: DrawProposalAnswer
    ): F[DrawProposalAnswerResult] = MonadThrow[F].raiseError(new Exception)

    override def forfeit(userId: UserId, gameId: GameId): F[ForfeitResult] = MonadThrow[F].raiseError(new Exception)
  }
}
