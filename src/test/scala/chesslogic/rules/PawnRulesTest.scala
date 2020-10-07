package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.rules.PawnRules.getPossibleMoves
import org.scalatest.flatspec.AnyFlatSpec

class PawnRulesTest extends AnyFlatSpec{
  val game = Game()


"Pawn on A2 before moving" should "have 2 possible moves" in {
  val startingBoard = game.gameHistory.head
  val position = Position(2,1)
  assertResult(2)(getPossibleMoves(position,startingBoard).size)
}
  "Pawn on A2 after moving to A3" should "have 1 possible move" in {
    val from = Position(2,1)
    val to = Position(3,1)
    val afterMove = game.makeMove(from,to)
    assert(afterMove.isDefined)
    assertResult(1)(getPossibleMoves(to,afterMove.get.currentBoard).size)



  }



}
