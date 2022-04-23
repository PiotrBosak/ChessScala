package chesslogic.rules
import chesslogic.board.Board
import chesslogic.board.position.{MoveType, Position}
import chesslogic.pieces.Queen


object QueenRules extends MovingRules[Queen] {
  override def getPossibleMoves(position: Position, board: Board): List[(MoveType, Position)] =
    RookRules.getPossibleMoves(position, board) ++ BishopRules.getPossibleMoves(position, board)

  override def getPossibleAttacks(position: Position, board: Board): List[(MoveType, Position)] =
    BishopRules.getPossibleAttacks(position, board) ++ RookRules.getPossibleAttacks(position, board)
}
