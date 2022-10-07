package gateway.domain

import io.circe.{ Codec, Encoder }
import monocle.Iso

object healthcheck {

  sealed trait Status
  object Status {
    case object Okay        extends Status
    case object Unreachable extends Status

    val _Bool: Iso[Status, Boolean] =
      Iso[Status, Boolean] {
        case Okay        => true
        case Unreachable => false
      }(if (_) Okay else Unreachable)

    implicit val jsonEncoder: Encoder[Status] =
      Encoder.forProduct1("status")(_.toString)
  }

  case class RedisStatus(value: Status)

  case class PostgresStatus(value: Status)

  case class AppStatus(
      redis: RedisStatus,
      postgres: PostgresStatus
  ) derives Codec.AsObject
}
