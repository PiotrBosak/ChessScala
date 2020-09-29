package chesslogic.pieces

import chesslogic.Color

sealed abstract class Piece(val color: Color)

case class Pawn(override val color: Color) extends Piece(color)

case class Bishop(override val color: Color) extends Piece(color)

case class Knight(override val color: Color) extends Piece(color)

case class Rook(override val color: Color) extends Piece(color)

case class Queen(override val color: Color) extends Piece(color)

case class King(override val color: Color) extends Piece(color)

