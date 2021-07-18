package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.rules.RookRules.{getPossibleAttacks, getPossibleMoves}
import org.scalatest.flatspec.AnyFlatSpec

class RookRulesTest extends AnyFlatSpec{
  val game = Game()

  "Rook on A1" should "have 0 moves possible at the start" in {
    val startingBoard = game.currentBoard
    val position = Position(1,1)
    assertResult(0)(getPossibleMoves(position,startingBoard).size)
  }
  "After pawn move, Rook on A1, after 1 move" should "have 9 possible moves" in {
    val afterPawn = game.makeMoveWithoutTurn(Position(2,1),Position(4,1)).get
    val afterFirstRookMove  = afterPawn.makeMoveWithoutTurn(Position(1,1),Position(3,1)).get
    assertResult(9)(getPossibleMoves(Position(3,1),afterFirstRookMove.currentBoard).size)

  }
"After capturing pawn on D7, Rook "should "have  2 possible attacks and 5 moves" in {
  val afterPawn = game.makeMoveWithoutTurn(Position(2,1),Position(4,1)).get
  val afterFirstRookMove  = afterPawn.makeMoveWithoutTurn(Position(1,1),Position(3,1)).get
  val afterSecondRookMove = afterFirstRookMove.makeMoveWithoutTurn(Position(3,1),Position(3,4)).get
  val afterAttack = afterSecondRookMove.makeMoveWithoutTurn(Position(3,4),Position(7,4)).get
  assertResult(3)(getPossibleAttacks(Position(7,4),afterAttack.currentBoard).size)
  assertResult(4)(getPossibleMoves(Position(7,4),afterAttack.currentBoard).size)
}

}
