package chesslogic.board

import chesslogic._
import chesslogic.pieces.Piece

case class Tile private(color: Color, position: Position, currentPiece: Option[Piece],hasMoved:Boolean = false) {


  def isEmpty: Boolean = currentPiece.isEmpty

  def hasPiece: Boolean = currentPiece.isDefined

  def isPieceColorDifferent(another: Option[Tile]): Boolean =
    another.isDefined && another.get.currentPiece.isDefined && another.get.currentPiece.get.color != this.color

  def isPieceColorDifferent(another:Tile): Boolean = isPieceColorDifferent(Some(another))

}

object Tile {
  def apply(color: Color, position: Position, startingPiece: Option[Piece]): Option[Tile] = {
    val potentialTile = Some(new Tile(color, position, startingPiece))
    for {
      tile <- potentialTile
      if position.column >= 1 && position.column <= 8
      if position.row >= 1 && position.column <= 8
      if ((position.row + position.column) % 2 == 0) || color == White
      if (position.row + position.column) % 2 == 1 || color == Black
    } yield tile
  }

}
