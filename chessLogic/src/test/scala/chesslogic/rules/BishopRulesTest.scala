package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.rules.BishopRules.getPossibleMoves
import org.scalatest.flatspec.AnyFlatSpec

class BishopRulesTest extends AnyFlatSpec{
  val game = Game()

  "Bishop on C1" should "have 0 moves possible at the start" in {
    val startingBoard = game.currentBoard
    val position = Position(1,3)
    assertResult(0)(getPossibleMoves(position,startingBoard).size)
  }
  "After pawn D2 moves Bishop on C1" should "have 5 possible moves" in  {
    val afterPawnMove = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    assertResult(5)(getPossibleMoves(Position(1,3),afterPawnMove.currentBoard).size)
    println(getPossibleMoves(Position(1,3),afterPawnMove.currentBoard))

  }
  "Bishop on C1" should "have 8 possible moves after moving to F4" in {
    val afterPawnMove = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    val afterFirstBishop = afterPawnMove.makeMoveWithoutTurn(Position(1,3),Position(4,6)).get
    assertResult(8)(getPossibleMoves(Position(4,6),afterFirstBishop.currentBoard).size)

  }






}
