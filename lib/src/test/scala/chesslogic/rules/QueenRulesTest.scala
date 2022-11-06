package lib.logic.rules

import lib.logic.rules.QueenRules.getPossibleMoves
import org.scalatest.flatspec.AnyFlatSpec
import lib.logic.game.FullGame
import lib.logic.board.Position
import lib.logic.board.File.*
import lib.logic.board.Rank.*

class QueenRulesTest extends AnyFlatSpec {

  val game: FullGame = FullGame()
  "Queen" should "have 0 possible moves at the beginning" in {
    val startingBoard = game.currentBoard
    assertResult(0)(getPossibleMoves(Position(D, Eight), startingBoard).size)
  }

  "Queen on D1" should "have 15 possible moves after moving to D3" in {
    val afterPawn  = game.makeMoveWithoutTurn(Position(D, Two), Position(D, Four)).get
    val afterFirst = afterPawn.makeMoveWithoutTurn(Position(D, One), Position(D, Three)).get
    assertResult(15)(getPossibleMoves(Position(D, Three), afterFirst.currentBoard).size)
  }

}