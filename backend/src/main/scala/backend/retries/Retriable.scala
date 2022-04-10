package backend.retries

import cats.syntax.all.*
import cats.*
import cats.derived.semiauto.{ derived, product, productOrder }
import io.circe.Codec

enum Retriable derives Codec.AsObject, Eq, Show:
  case Orders
  case Payments
