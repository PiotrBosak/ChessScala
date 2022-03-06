package chesslogic.board

import cats.implicits.*
import chesslogic.Color.*
import chesslogic.*
import chesslogic.pieces.Piece
import cats.derived.semiauto.*
import io.circe.Codec

case class Tile (color: Color, position: Position, currentPiece: Option[Piece], hasMoved: Boolean = false) derives Codec.AsObject {

  def isEmpty: Boolean = currentPiece.isEmpty

  def hasPiece: Boolean = currentPiece.isDefined

  def isPieceColorDifferent(another: Option[Tile]): Boolean =
    another.isDefined && another.get.currentPiece.isDefined && another.get.currentPiece.get.color != this.color

  def isPieceColorDifferent(another: Tile): Boolean = isPieceColorDifferent(Some(another))

}

object Tile {
  def apply(color: Color, position: Position, startingPiece: Option[Piece]): Option[Tile] = {

    ((position.column >= 1 && position.column <= 8) &&
      (position.row >= 1 && position.column <= 8) &&
      (((position.row + position.column) % 2 == 0) || color == White) &&
      ((position.row + position.column) % 2 == 1 || color == Black))
      .guard[Option]
      .as(new Tile(color, position, startingPiece))
  }
}
