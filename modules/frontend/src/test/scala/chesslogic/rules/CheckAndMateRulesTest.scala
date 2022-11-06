package lib.logic.rules

import lib.logic.Color.*
import lib.logic.board.File.*
import lib.logic.board.Rank.*
import lib.logic.rules.CheckAndMateRules.{ isKingChecked, isKingMated }
import lib.logic.board.Position
import lib.logic.game.FullGame
import org.scalatest.flatspec.AnyFlatSpec

class CheckAndMateRulesTest extends AnyFlatSpec {
  private val game = FullGame()
  "White king" should "be checked after black queen on A5" in {
    val afterWhitePawn =
      game.makeMoveWithoutTurn(Position(D, Two), Position(D, Four)).get
    val afterBlackPawn = afterWhitePawn
      .makeMoveWithoutTurn(Position(C, Seven), Position(C, Five))
      .get
    val afterQueen = afterBlackPawn
      .makeMoveWithoutTurn(Position(D, Eight), Position(A, Five))
      .get
    assert(isKingChecked(afterQueen.currentBoard, White))
    assert(!isKingMated(White, afterQueen))
  }
  "White king" should "be mated after fool's mate" in {
    val firstMove =
      game.makeMoveWithoutTurn(Position(F, Two), Position(F, Three)).get
    val secondMove =
      firstMove.makeMoveWithoutTurn(Position(E, Seven), Position(E, Five)).get
    val thirdMove =
      secondMove.makeMoveWithoutTurn(Position(G, Two), Position(G, Four)).get
    val fourthMove =
      thirdMove.makeMoveWithoutTurn(Position(D, Eight), Position(H, Four)).get
    assert(isKingMated(White, fourthMove))
  }

}
