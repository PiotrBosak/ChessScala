package gateway.algebras

import lib.domain.auth.UserId
import lib.domain.game.GameId
import gateway.domain.gamesearch.PollResult.*
import cats.{ Applicative, Monad, MonadThrow }
import cats.effect.std.Queue
import cats.syntax.all.*
import io.circe.syntax.*
import cats.effect.kernel.{ Concurrent, Ref, Resource }
import dev.profunktor.redis4cats.RedisCommands
import io.circe.Json
import lib.domain.auth.*
import lib.effects.RedisEncodeExt.asRedis
import gateway.domain.gamesearch.{ PollResult, StartSearchResult, StopSearchResult }
import skunk.Session

import java.util.UUID
import scala.util.Try

trait GameSearchAlg[F[_]] {

  def startSearch(userId: UserId): F[StartSearchResult]

  def poll(userId: UserId): F[PollResult]

  def stopSearching(userId: UserId): F[StopSearchResult]

}

object GameSearchAlg {
  def make[F[_]: MonadThrow](
      redis: RedisCommands[F, String, String],
      queue: Queue[F, UserId],
      cancellations: Ref[F, List[UserId]]
  ): GameSearchAlg[F] = new GameSearchAlg[F] {
    import StartSearchResult._
    import StopSearchResult._
    override def startSearch(userId: UserId): F[StartSearchResult] =
      redis.get(userId.asRedis).flatMap {
        case Some(_) =>
          Applicative[F].pure(GameAlreadyStarted)
        case None =>
          queue.offer(userId).as(SearchStartSuccessful)
      }

    override def poll(userId: UserId): F[PollResult] =
      redis
        .get(userId.asRedis)
        .attemptTap {
          case Left(value) =>
            Applicative[F].unit
          case Right(value) =>
            Applicative[F].unit
        }
        .flatMap {
          case Some(gameId) =>
            (Try(Json.fromString(gameId.show).as[UUID]))
            Json
              .fromString(gameId)
              .as[GameId]
              .liftTo[F]
              .map { gameId =>
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
