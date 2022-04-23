package chesslogic.game

import cats.data.NonEmptyList
import chesslogic.White
import chesslogic.board.Board
import chesslogic.board.position.Position
import chesslogic.game.FullGame.Turn
import chesslogic.game.FullGame.Turn.{BlackTurn, WhiteTurn}
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

case class FullGame(gameHistory:NonEmptyList[Board] = NonEmptyList.one(Board()), turn : Turn = WhiteTurn) {

  val currentBoard: Board = gameHistory.head


  def makeMove(from: Position, to: Position): Option[FullGame] = {
    val currentBoard        = gameHistory.head
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      possibleMoves   <- possibleMovesOption
      (moveType, _) <- possibleMoves.find(p => p._2 == to)
      tileToMove = currentBoard.getTile(to)
      tileFrom   = currentBoard.getTile(from)
      attackingPiece <- tileFrom.currentPiece
      isColorCorrect = if (attackingPiece.color == White) turn == WhiteTurn else turn == BlackTurn
      newBoard <- currentBoard.getBoardAfterMove(moveType, tileFrom, tileToMove, currentBoard) if isColorCorrect
    } yield FullGame(newBoard :: this.gameHistory, turn = turn.changeTurn)
  }

  def makeMoveWithoutTurn(from: Position, to: Position): Option[FullGame] = {
    val currentBoard        = gameHistory.head
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      possibleMoves   <- possibleMovesOption
      (moveType, _) <- possibleMoves.find(p => p._2 == to)
      tileToMove = currentBoard.getTile(to)
      tileFrom   = currentBoard.getTile(from)
      newBoard <- currentBoard.getBoardAfterMove(moveType, tileFrom, tileToMove, currentBoard)
    } yield FullGame(newBoard :: this.gameHistory, turn = turn.changeTurn)

  }

}

object FullGame {
   @derive(encoder,decoder)
  sealed trait Turn {
    def changeTurn : Turn = this match {
      case Turn.WhiteTurn => BlackTurn
      case Turn.BlackTurn => WhiteTurn
    }
  }
  object Turn {
    case object WhiteTurn extends Turn
    case object BlackTurn extends Turn
  }
}
