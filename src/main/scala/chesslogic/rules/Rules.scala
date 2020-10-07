package chesslogic.rules

import chesslogic.board.{Board, Position, Tile}

trait Rules {
  type ErrorOrPosition = Either[String,Position]
  def getPossibleMoves(position: Position,board:Board):List[Position]
  def getPossibleAttacks(position:Position,board:Board):List[Position]
}
