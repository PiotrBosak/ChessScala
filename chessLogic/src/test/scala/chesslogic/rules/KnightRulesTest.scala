package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import org.scalatest.flatspec.AnyFlatSpec

class KnightRulesTest extends AnyFlatSpec{
  private val game: Game = Game()

  "Knight on B8" should "have 2 possible moves" in {
    val startingBoard = game.currentBoard
    assertResult(2)(KnightRules.getPossibleMoves(Position(8,2),startingBoard).size)
  }

  "Knight on G1" can "capture be able to capture pawn on F7" in {
    val first = game.makeMoveWithoutTurn(Position(1,7),Position(3,6)).get
    val second = first.makeMoveWithoutTurn(Position(3,6),Position(5,5)).get
    val third = second.makeMoveWithoutTurn(Position(5,5),Position(7,6)).get
    assertResult(31)(third.currentBoard.tiles.values.count(_.hasPiece))
    assertResult(2)(KnightRules.getPossibleAttacks(Position(7,6),third.currentBoard).size)

  }

}
