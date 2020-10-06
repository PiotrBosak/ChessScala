package chesslogic.rules

import chesslogic.board.{Board, Position, Tile}

trait Rules {

  def getPossibleMoves(position: Position)(implicit board:Board):List[Position]
  def getPossibleAttacks(position:Position)(implicit board:Board):List[Position]

}
