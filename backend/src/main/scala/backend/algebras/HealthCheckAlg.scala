package backend.algebras

import backend.domain.auth.UserId
import backend.domain.game.GameId
import backend.domain.healthcheck._
import cats.effect._
import cats.effect.implicits._
import cats.syntax.all._
import dev.profunktor.redis4cats.RedisCommands
import skunk._
import skunk.codec.all._
import skunk.implicits._

import scala.concurrent.duration._

trait HealthCheckAlg[F[_]] {
  def status: F[AppStatus]

}

object HealthCheckAlg {
  def make[F[_]: Temporal](
                            postgres: Resource[F, Session[F]],
                            redis: RedisCommands[F, String, String]
                          ): HealthCheckAlg[F] =
    new HealthCheckAlg[F] {

      val q: Query[Void, Int] =
        sql"SELECT pid FROM pg_stat_activity".query(int4)

      val redisHealth: F[RedisStatus] =
        redis.ping
          .map(_.nonEmpty)
          .timeout(1.second)
          .map(Status._Bool.reverseGet)
          .orElse(Status.Unreachable.pure[F].widen)
          .map(RedisStatus.apply)

      val postgresHealth: F[PostgresStatus] =
        postgres
          .use(_.execute(q))
          .map(_.nonEmpty)
          .timeout(1.second)
          .map(Status._Bool.reverseGet)
          .orElse(Status.Unreachable.pure[F].widen)
          .map(PostgresStatus.apply)

      val status: F[AppStatus] =
        (redisHealth, postgresHealth).parMapN(AppStatus.apply)
    }

}
