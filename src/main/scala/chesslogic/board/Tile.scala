package chesslogic.board

import chesslogic._
import chesslogic.pieces.Piece

case class Tile private(color: Color, position: Position, startingPiece: Option[Piece], currentPiece: Option[Piece]) {


  def isEmpty: Boolean = currentPiece.isEmpty

  def hasPiece: Boolean = currentPiece.isDefined

  def hasMoved: Boolean = currentPiece eq startingPiece

  def isPieceColorDifferent(another: Option[Tile]): Boolean =
    another.isDefined && another.get.currentPiece.isDefined && another.get.currentPiece.get.color != this.color
}

object Tile {
  def apply(color: Color, position: Position, startingPiece: Option[Piece], currentPiece: Option[Piece]): Option[Tile] = {
    val potentialTile = Some(new Tile(color, position, startingPiece, currentPiece))
    for {
      tile <- potentialTile
      if position.column >= 1 && position.column <= 8
      if position.row >= 1 && position.column <= 8
      if ((position.row + position.column) % 2 == 0) || color == Black
      if (position.row + position.column) % 2 == 1 || color == White
    } yield tile
  }


  def apply(color: Color, position: Position, startingPiece: Option[Piece]): Option[Tile] =
    apply(color, position, startingPiece, startingPiece)
}
