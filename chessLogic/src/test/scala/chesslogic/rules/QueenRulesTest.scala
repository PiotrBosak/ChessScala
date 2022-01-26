package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.FullGame
import chesslogic.rules.QueenRules.getPossibleMoves
import org.scalatest.flatspec.AnyFlatSpec

class QueenRulesTest extends AnyFlatSpec{

  val game: FullGame = FullGame()
  "Queen" should "have 0 possible moves at the beginning" in {
    val startingBoard = game.currentBoard
    assertResult(0)(getPossibleMoves(Position(8,4),startingBoard).size)
  }

  "Queen on D1" should "have 15 possible moves after moving to D3" in {
    val afterPawn = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    val afterFirst = afterPawn.makeMoveWithoutTurn(Position(1,4),Position(3,4)).get
    assertResult(15)(getPossibleMoves(Position(3,4),afterFirst.currentBoard).size)
  }


}
