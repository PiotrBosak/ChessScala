package lib.routes

import cats.Monad
import gateway.algebras.HealthCheckAlg
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl

final case class HealthcheckRoutes[F[_]: Monad](
    healthCheck: HealthCheckAlg[F]
) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/healtcheck"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root =>
    Ok(healthCheck.status)
  }
}
