package backend.algebras

import backend.domain.healthcheck.AppStatus


trait HealthCheck[F[_]] {
  def status: F[AppStatus]
}
