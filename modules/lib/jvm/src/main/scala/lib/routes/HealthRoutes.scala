package lib.routes

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

final class HealthRoutes[F[_]: Monad] extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "health" => Ok()
  }

}