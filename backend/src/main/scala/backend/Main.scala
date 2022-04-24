package backend

import backend.config.Config
import backend.domain.auth.UserId
import backend.modules.{Algebras, HttpApi, Security}
import backend.resources.{AppResources, MkHttpServer}
import cats.effect._
import cats.effect.std.{Queue, Random, Supervisor}
import dev.profunktor.redis4cats.log4cats._
import eu.timepit.refined.auto._
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  implicit val logger = Slf4jLogger.getLogger[IO]

  override def run: IO[Unit] =
    Config.load[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg") >>
        Supervisor[IO].use { implicit sp =>
          Resource.liftK[IO](Ref.of[IO, List[UserId]](List.empty[UserId])).flatMap { cancellations =>
            Resource.liftK[IO](Queue.unbounded[IO, UserId]).flatMap { queue =>
              Resource.eval(Random.javaUtilRandom[IO](new java.util.Random)).flatMap { implicit random =>
                AppResources
                  .make[IO](cfg)
                  .evalMap { res =>
                    Security.make[IO](cfg, res.postgres, res.redis).flatMap { security =>
                      val services = Algebras.make[IO](res.postgres, res.redis, cancellations, queue)
                      val api = HttpApi.make[IO](services, security)
                      services.gameMatcherAlg.matchGames.start
                        .as(cfg.httpServerConfig -> api.httpApp)
                    }
                  }
              }
            }
          }
            .flatMap {
              case (cfg, httpApp) =>
                MkHttpServer[IO].newEmber(cfg, httpApp)
            }
            .useForever
        }
    }

}