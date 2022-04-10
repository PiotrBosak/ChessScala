package backend.modules

import io.odin.Logger
import backend.algebras.GameSearchAlg
import backend.http.auth.users.CommonUser
import backend.http.jwt.JwtAuthMiddleware
import backend.http.routes.auth.*
import backend.http.routes.*
import backend.http.routes.game.{ GameRoutes, GameSearchRoutes }
import cats.effect.Async
import cats.syntax.all.*
import org.http4s.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.*

import scala.annotation.nowarn
import scala.concurrent.duration.*

object HttpApi {

  def make[F[_]: Async: Logger](
      algebras: Algebras[F],
      security: Security[F]
  ): HttpApi[F] = new HttpApi[F](algebras, security) {}
}

sealed abstract class HttpApi[F[_]: Async: Logger] private (algebras: Algebras[F], security: Security[F]) {
  private val usersMiddleware =
    JwtAuthMiddleware[F, CommonUser](security.userJwtAuth.value, security.usersAuth.findUser)

  private val loginRoutes  = LoginRoutes[F](security.auth).routes
  private val logoutRoutes = LogoutRoutes[F](security.auth).routes(usersMiddleware)
  private val userRoutes   = UserRoutes[F](security.auth).routes

  private val gameRoutes       = GameRoutes[F](algebras.gameAlg).routes(usersMiddleware)
  private val gameSearchRoutes = GameSearchRoutes[F](algebras.gameSearchAlg).routes(usersMiddleware)

  private val openRoutes: HttpRoutes[F] = {
    //is it really a bug?
    // when I make logoutRoutes before user and login(which don't go through jwt middleware, I get 403 (bearer not found)
    //even though they don't need one
    userRoutes <+> loginRoutes <+> logoutRoutes <+> gameRoutes <+> gameSearchRoutes
  }

  private val routes: HttpRoutes[F] = Router(
    "/v1" -> openRoutes
  )

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { (http: HttpRoutes[F]) =>
      AutoSlash(http)
    } andThen { (http: HttpRoutes[F]) =>
      @nowarn
      val config = CORSConfig.default
        .withAnyOrigin(false)
        .withAllowedOrigins(Set("http://localhost:8000"))
      @nowarn
      val cors = CORS(http, config)
      cors
    } andThen { (http: HttpRoutes[F]) =>
      Timeout(60.seconds)(http)
    }
  }
  private val loggers: HttpApp[F] => HttpApp[F] = {
    { (http: HttpApp[F]) =>
      RequestLogger.httpApp(true, true)(http)
    } andThen { (http: HttpApp[F]) =>
      ResponseLogger.httpApp(true, true)(http)
    }
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)

}
