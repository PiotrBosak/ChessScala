package chesslogic.board

import io.circe.Codec
import cats.syntax.all.*
import cats.*
import cats.derived.semiauto.{derived, product, productOrder}
case class Move(from:Tile,to:Tile) derives Codec.AsObject
