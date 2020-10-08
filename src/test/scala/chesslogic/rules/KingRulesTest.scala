package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import org.scalatest.flatspec.AnyFlatSpec

class KingRulesTest extends AnyFlatSpec{
  private val game = Game()
  "King on E1" should "have 0 moves" in {
    val startingBoard = game.currentBoard
    assertResult(0)(KingRules.getPossibleMoves(Position(1,5),startingBoard).size)
  }

  "King on E8" should "capture pawn on F7 and have 5 possible attacks" in {
    val afterPawn = game.makeMoveWithoutTurn(Position(2,5),Position(4,5)).get
    val first = afterPawn.makeMoveWithoutTurn(Position(1,5),Position(2,5)).get
    val second = first.makeMoveWithoutTurn(Position(2,5),Position(3,5)).get
    val third = second.makeMoveWithoutTurn(Position(3,5),Position(4,6)).get
    val fourth = third.makeMoveWithoutTurn(Position(4,6),Position(5,6)).get
    val fifth= fourth.makeMoveWithoutTurn(Position(5,6),Position(6,6)).get
    val sixth= fifth.makeMoveWithoutTurn(Position(6,6),Position(7,6)).get
    assertResult(5)(KingRules.getPossibleAttacks(Position(7,6),sixth.currentBoard).size)
    assertResult(31)(sixth.currentBoard.tiles.values.count(_.hasPiece))

  }

}
