package lib.logic.rules

import lib.logic.board.{Tile, Position, Board, MoveType}

trait MovingRules[Piece] {
  def getPossibleMoves(position: Position, board: Board): List[(MoveType, Position)]
  def getPossibleAttacks(position: Position, board: Board): List[(MoveType, Position)]
}
