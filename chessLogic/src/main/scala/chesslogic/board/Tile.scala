package chesslogic.board

import cats.implicits._
import chesslogic._
import chesslogic.board.position.Position
import chesslogic.pieces.Piece
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive


@derive(encoder,decoder)
case class Tile (position: Position, currentPiece: Option[Piece], hasMoved: Boolean = false) {

  def isEmpty: Boolean = currentPiece.isEmpty

  def hasPiece: Boolean = currentPiece.isDefined

  val color: Color =
    if ((position.file.toNumber + position.rank.toNumber) % 2 == 0) White
    else Black

  def isPieceColorDifferent(another: Option[Tile]): Boolean =
    another.isDefined && another.get.currentPiece.isDefined && another.get.currentPiece.get.color != this.color

  def isPieceColorDifferent(another: Tile): Boolean = isPieceColorDifferent(Some(another))

}
