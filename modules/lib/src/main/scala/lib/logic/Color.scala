package lib.logic

import cats.syntax.all.*
import cats.*
import cats.derived.semiauto.{ derived, product, productOrder }
import io.circe.Codec

enum Color derives Codec.AsObject, Eq, Show:
  case White
  case Black
