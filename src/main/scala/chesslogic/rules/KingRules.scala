package chesslogic.rules

import chesslogic.board.{Board, Position}
import chesslogic.rules.RulesForKingAndKnight.getAllMoves

object KingRules extends MovingRules {
  private val combinations = (for {
    x <- -1 to 1
    y <- -1 to 1
  } yield (x, y)).distinct.toList


  override def getPossibleMoves(position: Position, board: Board): List[Position] =
    getAllMoves(position, board,combinations)._1

  override def getPossibleAttacks(position: Position, board: Board): List[Position] =
    getAllMoves(position, board,combinations)._2



}
