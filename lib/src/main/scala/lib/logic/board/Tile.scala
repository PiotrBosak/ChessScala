package lib.logic.board

import math.Ordered.orderingToOrdered
import cats.implicits.*
import lib.logic.Color.*
import lib.logic.rules.*
import cats.derived.*
import lib.logic.pieces.Piece
import lib.logic.Color
import io.circe.Codec

case class Tile(
    position: Position,
    currentPiece: Option[Piece],
    hasMoved: Boolean = false
) derives Codec.AsObject {

  def isEmpty: Boolean = currentPiece.isEmpty

  def hasPiece: Boolean = currentPiece.isDefined

  val color: Color =
    if ((position.file.toNumber + position.rank.toNumber) % 2 == 1) White
    else Black

  def isPieceColorDifferent(another: Option[Tile]): Boolean =
    another.isDefined && another.get.currentPiece.isDefined && another.get.currentPiece.get.color != this.color

  def isPieceColorDifferent(another: Tile): Boolean = isPieceColorDifferent(
    Some(another)
  )

}
object Tile:
  given Ordering[Tile] = (x: Tile, y: Tile) => x.position.compare(y.position)
