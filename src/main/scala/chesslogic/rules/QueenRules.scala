package chesslogic.rules
import chesslogic.board.{Board, Position}
import chesslogic.pieces.Queen

object QueenRules extends MovingRules[Queen] {
  override def getPossibleMoves(position: Position, board: Board): List[Position] =
    RookRules.getPossibleMoves(position,board) ++ BishopRules.getPossibleMoves(position,board)

  override def getPossibleAttacks(position: Position, board: Board): List[Position] =
    BishopRules.getPossibleAttacks(position,board) ++ RookRules.getPossibleAttacks(position, board)
}
