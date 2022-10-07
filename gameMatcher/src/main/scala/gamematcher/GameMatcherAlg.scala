package backend.algebras

import io.odin.Logger
import backend.domain.auth.UserId
import backend.domain.game.{GameId, PvPGame}
import backend.effects.GenUUID
import cats.Applicative
import cats.syntax.all.*
import cats.effect.kernel.{Ref, Resource, Temporal}
import cats.effect.std.{Queue, Random}
import backend.domain.RedisEncodeExt.asRedis
import dev.profunktor.redis4cats.RedisCommands
import lib.logic.board.Board
import lib.logic.game
import lib.logic.game.SimpleGame
import io.circe.syntax.*
import skunk.Session

import scala.concurrent.duration.FiniteDuration

trait GameMatcherAlg[F[_]] {

  def matchGames: F[Unit]
}

object GameMatcherAlg {
  def make[F[_]: Temporal: GenUUID: Logger: Random](
      postgres: Resource[F, Session[F]],
      redis: RedisCommands[F, String, String],
      cancellations: Ref[F, List[UserId]],
      queue: Queue[F, UserId],
      waitPeriod: FiniteDuration
  ): GameMatcherAlg[F] = new GameMatcherAlg[F] {
    override def matchGames: F[Unit] =
      (getFromQueue, getFromQueue).mapN { case (fst, snd) =>
        GenUUID[F].make
          .map(id => GameId.apply(id))
          .flatMap { gameId =>
            saveGame(fst, snd, gameId) >>
              Logger[F].info {
                s"found game between $fst and $snd"
              } >>
              redis.set(fst.asRedis, gameId.asRedis) *> redis.set(snd.asRedis, gameId.asRedis)
          }
      }.flatten >> Temporal[F].sleep(waitPeriod) >> matchGames

    private def saveGame(first: UserId, second: UserId, gameId: GameId): F[Unit] = {
      Random[F].nextBoolean.flatMap { b =>
        val (white, black) = if (b) (first, second) else (second, first)
        redis.set(gameId.asRedis, PvPGame(white, black, gameId, game.SimpleGame(Board())).asRedis)
      }
    }

    private def getFromQueue: F[UserId] =
      queue.take.flatMap { userId =>
        cancellations
          .modify { list =>
            if (list.contains(userId))
              (list diff List(userId), true)
            else (list, false)
          }
          .flatMap {
            if (_) getFromQueue
            else Applicative[F].pure(userId)
          }
      }
  }

}
