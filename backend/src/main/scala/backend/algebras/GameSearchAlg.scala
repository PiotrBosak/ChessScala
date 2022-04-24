package backend.algebras

import backend.domain.auth.UserId
import backend.domain.game.GameId
import backend.domain.gamesearch._
import cats.{Applicative, Monad, MonadThrow}
import cats.effect.std.Queue
import backend.domain.RedisEncode._
import backend.domain.RedisEncode
import cats.syntax.all._
import io.circe.syntax._
import cats.effect.kernel.{Concurrent, Ref, Resource}
import dev.profunktor.redis4cats.RedisCommands
import io.circe.Json
import skunk.Session

import java.util.UUID

trait GameSearchAlg[F[_]] {

  def startSearch(userId: UserId): F[StartSearchResult]

  def poll(userId: UserId): F[PokeResult]

  def stopSearching(userId: UserId): F[StopSearchResult]

}

object GameSearchAlg {
  def make[F[_] : MonadThrow](redis: RedisCommands[F, String, String],
                              queue: Queue[F, UserId],
                              cancellations: Ref[F, List[UserId]],
                             ): GameSearchAlg[F] = new GameSearchAlg[F] {
    override def startSearch(userId: UserId): F[StartSearchResult] =
      redis.get(userId.asRedis).flatMap {
        case Some(_) =>
          Applicative[F].pure(GameAlreadyStarted)
        case None =>
          queue.offer(userId).as(SearchStartSuccessful)
      }

    override def poll(userId: UserId): F[PokeResult] =
      redis.get(userId.asRedis).flatMap {
        case Some(gameId) =>
          Json.fromString(gameId).as[GameId].liftTo[F].map { gameId =>
            GameFound(gameId)
          }
        case None =>
          Applicative[F].pure(GameNotFoundYet)
      }

    override def stopSearching(userId: UserId): F[StopSearchResult] =
      redis.get(userId.asRedis).flatMap {
        case Some(_) =>
          Applicative[F].pure(GameAlreadyFound)
        case None =>
          cancellations
            .update(userId :: _)
            .as(SearchStopSuccessful)
      }

  }

}