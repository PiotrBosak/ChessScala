package chesslogic.rules

import chesslogic.board.Board
import chesslogic.board.position.{MoveType, Position}


trait MovingRules[Piece]  {

  def getPossibleMoves(position: Position,board:Board):List[(MoveType,Position)]
  def getPossibleAttacks(position:Position,board:Board):List[(MoveType,Position)]
}
