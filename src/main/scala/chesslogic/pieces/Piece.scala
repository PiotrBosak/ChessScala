package chesslogic.pieces

import chesslogic.Color

sealed trait Piece{
  val color:Color
}

case class Pawn(override val color: Color) extends Piece

case class Bishop(override val color: Color) extends Piece

case class Knight(override val color: Color) extends Piece

case class Rook(override val color: Color) extends Piece

case class Queen(override val color: Color) extends Piece

case class King(override val color: Color) extends Piece

