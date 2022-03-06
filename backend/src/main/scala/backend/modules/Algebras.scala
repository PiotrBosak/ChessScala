package backend.modules

import io.odin.Logger
import backend.algebras.{GameAlg, GameMatcherAlg, GameSearchAlg, HealthCheckAlg, UserAlg}
import backend.domain.auth.UserId
import backend.domain.game.{GameId, PvPGame}
import backend.effects.GenUUID
import cats.effect.kernel.{Ref, Resource, Temporal}
import cats.effect.std.{Queue, Random}
import chesslogic.board.Board
import dev.profunktor.redis4cats.RedisCommands

import scala.concurrent.duration.*
import skunk.Session

sealed abstract class Algebras[F[_]] private(
                                              val gameAlg: GameAlg[F],
                                              val gameSearchAlg: GameSearchAlg[F],
                                              val gameMatcherAlg: GameMatcherAlg[F],
                                              val healthCheckAlg: HealthCheckAlg[F],
                                              val userAlg: UserAlg[F]
                                            ) {}

object Algebras {
  def make[F[_] : GenUUID : Temporal: Logger: Random](
                                       postgres: Resource[F, Session[F]],
                                       redis: RedisCommands[F, String, String],
                                       cancellations: Ref[F, List[UserId]],
                                       queue: Queue[F, UserId],
                                     ): Algebras[F] = {
    new Algebras[F](
      GameAlg.make(postgres, redis),
      GameSearchAlg.make(redis, queue, cancellations),
      GameMatcherAlg.make(postgres,redis,cancellations,queue,1.second),
      HealthCheckAlg.make(postgres, redis),
      UserAlg.make(postgres)
    ) {}
  }
}
