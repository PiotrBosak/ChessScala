package chesslogic.rules

import chesslogic.White
import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.rules.CheckAndMateRules.{isKingChecked, isKingMated}
import org.scalatest.flatspec.AnyFlatSpec

class CheckAndMateRulesTest extends AnyFlatSpec{
  private val game = Game()
  "White king" should "be checked after black queen on A5" in {
    val afterWhitePawn = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    val afterBlackPawn = afterWhitePawn.makeMoveWithoutTurn(Position(7,3),Position(5,3)).get
    val afterQueen = afterBlackPawn.makeMoveWithoutTurn(Position(8,4),Position(5,1)).get
    assert(isKingChecked(afterQueen.currentBoard,White))
    assert(!isKingMated(White,afterQueen))
  }
  "White king" should "be mated after fool's mate" in {
    val firstMove = game.makeMoveWithoutTurn(Position(2,6),Position(3,6)).get
    val secondMove = firstMove.makeMoveWithoutTurn(Position(7,5),Position(5,5)).get
    val thirdMove = secondMove.makeMoveWithoutTurn(Position(2,7),Position(4,7)).get
    val fourthMove = thirdMove.makeMoveWithoutTurn(Position(8,4),Position(4,8)).get
    assert(isKingMated(White,fourthMove))
  }




}
