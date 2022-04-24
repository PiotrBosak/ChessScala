package backend.algebras

import backend.domain.RedisEncode.RedisEncodeExt
import backend.domain.auth.UserId
import backend.domain.game.{GameId, PvPGame}
import backend.effects.GenUUID
import cats.Applicative
import cats.effect.kernel.{Ref, Temporal}
import cats.effect.std.{Queue, Random}
import cats.syntax.all._
import chesslogic.board.Board
import chesslogic.game.SimpleGame
import dev.profunktor.redis4cats.RedisCommands
import org.typelevel.log4cats.Logger

import scala.concurrent.duration.FiniteDuration

trait GameMatcherAlg[F[_]] {

  def matchGames: F[Unit]
}

object GameMatcherAlg {
  def make[F[_] : Temporal : GenUUID : Logger : Random](
                                                         redis: RedisCommands[F, String, String],
                                                         cancellations: Ref[F, List[UserId]],
                                                         queue: Queue[F, UserId],
                                                         waitPeriod: FiniteDuration
                                                       ): GameMatcherAlg[F] = new GameMatcherAlg[F] {
    override def matchGames: F[Unit] = {
      (getFromQueue, getFromQueue).mapN {
        case (fst, snd) =>
          for {
            gameId <- createNewGameId
            _ <- saveGame(fst, snd, gameId)
            _ <- Logger[F].info(s"found game between $fst and $snd")
            _ <- redis.set(fst.asRedis, gameId.asRedis)
            _ <- redis.set(snd.asRedis, gameId.asRedis)
            _ <- Temporal[F].sleep(waitPeriod)
          }
          yield ()
      }.flatten >> matchGames
    }

    private def saveGame(first: UserId, second: UserId, gameId: GameId): F[Unit] = {
      Random[F].nextBoolean.flatMap { b =>
        val (white, black) = if (b) (first, second) else (second, first)
        redis.set(gameId.asRedis, PvPGame(white, black, gameId, SimpleGame(Board())).asRedis)
      }
    }

    private def createNewGameId: F[GameId] = GenUUID[F].make.map(GameId.apply)

    private def getFromQueue: F[UserId] =
      queue.take.flatMap { userId =>
        cancellations.modify { list =>
          if (list.contains(userId))
            (list diff List(userId), true)
          else (list, false)
        }.flatMap {
          if (_) getFromQueue
          else Applicative[F].pure(userId)
        }
      }
  }

}
