package chesslogic.rules

import chesslogic.board.{Board, Position, Tile}

trait MovingRules[Piece]  {
  def getPossibleMoves(position: Position,board:Board):List[Position]
  def getPossibleAttacks(position:Position,board:Board):List[Position]
}
