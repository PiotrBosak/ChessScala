package lib.logic.pieces

import lib.logic.Color

sealed trait Piece {
  val color: Color
}
object Piece {
  final case class Pawn(override val color: Color)   extends Piece
  final case class Bishop(override val color: Color) extends Piece
  final case class Knight(override val color: Color) extends Piece
  final case class Rook(override val color: Color)   extends Piece
  final case class Queen(override val color: Color)  extends Piece
  final case class King(override val color: Color)   extends Piece
}
