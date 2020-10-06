package chesslogic.rules

import chesslogic.board.{Board, Position}
import chesslogic.pieces.Pawn

object PawnRules extends Rules{
  override def getPossibleAttacks(position: Position)(implicit board: Board): List[Position] = ???

  override def getPossibleMoves(position: Position)(implicit board: Board): List[Position] = ???


}
