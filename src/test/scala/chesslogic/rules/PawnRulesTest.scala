package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.rules.PawnRules.{getPossibleAttacks, getPossibleMoves}
import org.scalatest.flatspec.AnyFlatSpec

class PawnRulesTest extends AnyFlatSpec{
 private  val game = Game()


"Pawn on A2 before moving" should "have 2 possible moves" in {
  val startingBoard = game.currentBoard
  val position = Position(2,1)
  assertResult(2)(getPossibleMoves(position,startingBoard).size)
}
  "Pawn on A2 after moving to A3" should "have 1 possible move" in {
    val from = Position(2,1)
    val to = Position(3,1)
    val afterMove = game.makeMoveWithoutTurn(from,to)
    assert(afterMove.isDefined)
    assertResult(1)(getPossibleMoves(to,afterMove.get.currentBoard).size)

  }

  "Pawn on A2" can "capture pawn on B7" in {
    val startingPosition = Position(2,1)
    val firstPos = Position(4,1)
    val secondPos = Position(5,1)
    val thirdPos = Position(6,1)
    val fourthPosition = Position(7,2)
    val afterFirst = game.makeMoveWithoutTurn(startingPosition,firstPos).get
    val afterSecond = afterFirst.makeMoveWithoutTurn(firstPos,secondPos).get
    val afterThird = afterSecond.makeMoveWithoutTurn(secondPos,thirdPos).get
    assertResult(1)(getPossibleAttacks(thirdPos,afterThird.currentBoard).size)
    val afterFourth = afterThird.makeMoveWithoutTurn(thirdPos,fourthPosition).get
    assertResult(2)(getPossibleAttacks(fourthPosition,afterFourth.currentBoard).size)
    assertResult(31)(afterFourth.currentBoard.tiles.values.count(_.hasPiece))




  }



}
