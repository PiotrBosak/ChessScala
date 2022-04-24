package backend.modules

import backend.algebras.GameSearchAlg
import backend.http.auth.users.CommonUser
import backend.http.routes.auth._
import backend.http.routes._
import backend.http.routes.game.{GameRoutes, GameSearchRoutes}
import cats.effect.Async
import cats.syntax.all._
import dev.profunktor.auth.JwtAuthMiddleware
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.middleware._
import scala.concurrent.duration._

object HttpApi {

  def make[F[_] : Async](
                          algebras: Algebras[F],
                          security: Security[F]
                        ): HttpApi[F] = new HttpApi[F](algebras, security) {}
}

sealed abstract class HttpApi[F[_] : Async] private(
                                                     algebras: Algebras[F],
                                                     security: Security[F]) {
  private val usersMiddleware =
    JwtAuthMiddleware[F, CommonUser](security.userJwtAuth.value, security.usersAuth.findUser)


  private val userRoutes = UserRoutes[F](security.auth).routes
  private val loginRoutes = LoginRoutes[F](security.auth).routes
  private val logoutRoutes = LogoutRoutes[F](security.auth).routes(usersMiddleware)

  private val gameRoutes = GameRoutes[F](algebras.gameAlg).routes(usersMiddleware)
  private val gameSearch = GameSearchRoutes[F](algebras.gameSearchAlg).routes(usersMiddleware)

  //first those that don't need a token, then the rest
  private val openRoutes: HttpRoutes[F] =
    userRoutes <+> loginRoutes <+> logoutRoutes <+> gameRoutes <+> gameSearch

  private val routes: HttpRoutes[F] = Router(
    "/v1" -> openRoutes
  )

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { http: HttpRoutes[F] =>
      AutoSlash(http)
    } andThen { http: HttpRoutes[F] =>
      CORS(http)
    } andThen { http: HttpRoutes[F] =>
      Timeout(60.seconds)(http)
    }
  }
  private val loggers: HttpApp[F] => HttpApp[F] = {
    { http: HttpApp[F] =>
      RequestLogger.httpApp(true, true)(http)
    } andThen { http: HttpApp[F] =>
      ResponseLogger.httpApp(true, true)(http)
    }
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)

}
