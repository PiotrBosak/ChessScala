package chesslogic.rules
import chesslogic.board.{Board, Position}
import chesslogic.rules.RulesForKingAndKnight.getAllMoves

object KnightRules extends MovingRules {
  private val combinations:List[(Int,Int)] = List((2,1),(1,2),(2,-1),(1,-2),(-2,1),(-2,-1),(-1,-2),(-1,2))

  override def getPossibleMoves(position: Position, board: Board): List[Position] = getAllMoves(position, board,combinations)._1

  override def getPossibleAttacks(position: Position, board: Board): List[Position] = getAllMoves(position, board,combinations)._2













}
