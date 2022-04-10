package chesslogic.rules

import chesslogic.board.{ Board, Position, Tile, MoveType }

trait MovingRules[Piece] {
  def getPossibleMoves(position: Position, board: Board): List[(MoveType, Position)]
  def getPossibleAttacks(position: Position, board: Board): List[(MoveType, Position)]
}
