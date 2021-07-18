package chesslogic.rules

import chesslogic.White
import chesslogic.board.Position
import chesslogic.game.Game
import chesslogic.pieces.Rook
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
    val fifth= fourth.makeMoveWithoutTurn(Position(7,7),Position(5,7)).get
    val sixth= fifth.makeMoveWithoutTurn(Position(5,6),Position(5,7)).get
    assertResult(0)(KingRules.getPossibleAttacks(Position(5,7),sixth.currentBoard).size)
    assertResult(8)(KingRules.getPossibleMoves(Position(5,7),sixth.currentBoard).size)
    assertResult(31)(sixth.currentBoard.tiles.values.count(_.hasPiece))

  }

  "King on E8" can "make castling after queen,bishop and knight are gone" in {
    val afterPawn = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    val afterBishop = afterPawn.makeMoveWithoutTurn(Position(1,3),Position(6,8)).get
    val afterQueen = afterBishop.makeMoveWithoutTurn(Position(1,4),Position(3,4)).get
    val afterKnight = afterQueen.makeMoveWithoutTurn(Position(1,2),Position(3,1)).get
    assertResult(3)(KingRules.getPossibleMoves(Position(1,5),afterKnight.currentBoard).size)
    val afterCastling = afterKnight.makeMoveWithoutTurn(Position(1,5),Position(1,2)).get
    val rookTile = afterCastling.currentBoard.getTile(Position(1,3)).get
    assert(rookTile.currentPiece.isDefined && rookTile.currentPiece.get.isInstanceOf[Rook])
  }
  "King" should "not be able to do castling when he is checked" in {
    val afterPawn = game.makeMoveWithoutTurn(Position(2,4),Position(4,4)).get
    val afterBishop = afterPawn.makeMoveWithoutTurn(Position(1,3),Position(6,8)).get
    val afterQueen = afterBishop.makeMoveWithoutTurn(Position(1,4),Position(3,4)).get
    val afterKnight = afterQueen.makeMoveWithoutTurn(Position(1,2),Position(3,1)).get
    val afterBlackPawn = afterKnight.makeMoveWithoutTurn(Position(7,3),Position(6,3)).get
    val afterCheck = afterBlackPawn.makeMoveWithoutTurn(Position(8,4),Position(5,1)).get
    assert(CheckAndMateRules.isKingChecked(afterCheck.currentBoard,White))
    assert(afterCheck.makeMoveWithoutTurn(Position(1,5),Position(1,2)).isEmpty)
  }



}
