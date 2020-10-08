package chesslogic.rules
import chesslogic.board.{Board, Position}

object QueenRules extends Rules {
  override def getPossibleMoves(position: Position, board: Board): List[Position] =
    RookRules.getPossibleMoves(position,board) ++ BishopRules.getPossibleMoves(position,board)

  override def getPossibleAttacks(position: Position, board: Board): List[Position] =
    BishopRules.getPossibleAttacks(position,board) ++ RookRules.getPossibleAttacks(position, board)
}
