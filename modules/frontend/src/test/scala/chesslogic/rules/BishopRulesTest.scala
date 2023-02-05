package lib.logic.rules

import lib.logic.board.File.*
import lib.logic.board.Rank.*
import lib.logic.rules.BishopRules.getPossibleMoves
import lib.logic.board.Position
import lib.logic.game.FullGame
import org.scalatest.flatspec.AnyFlatSpec

class BishopRulesTest extends AnyFlatSpec {
  val game = FullGame()

  "Bishop on C1" should "have 0 moves possible at the start" in {
    val startingBoard = game.currentBoard
    val position      = Position(C, One)
    assertResult(0)(getPossibleMoves(position, startingBoard).size)
  }
  "After pawn D2 moves Bishop on C1" should "have 5 possible moves" in {
    val afterPawnMove =
      game.makeMoveWithoutTurn(Position(D, Two), Position(D, Four)).get
    assertResult(5)(
      getPossibleMoves(Position(C, One), afterPawnMove.currentBoard).size
    )

  }
  "Bishop on C1" should "have 8 possible moves after moving to F4" in {
    val afterPawnMove =
      game.makeMoveWithoutTurn(Position(D, Two), Position(D, Four)).get
    val afterFirstBishop =
      afterPawnMove.makeMoveWithoutTurn(Position(C, One), Position(F, Four)).get
    assertResult(8)(
      getPossibleMoves(Position(F, Four), afterFirstBishop.currentBoard).size
    )

  }

}