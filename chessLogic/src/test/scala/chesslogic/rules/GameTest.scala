package chesslogic.rules

import chesslogic.board.Position
import chesslogic.game.FullGame
import org.scalatest.flatspec.AnyFlatSpec

class GameTest extends AnyFlatSpec{
  private val game = FullGame()

  "white pawn" should "not be able to move twice" in {
    val afterFirstMove = game.makeMove(Position(2,1),Position(3,1)).get
    assert(afterFirstMove.makeMove(Position(3,1),Position(4,1)).isEmpty)
  }

}
