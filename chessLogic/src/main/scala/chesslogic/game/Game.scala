package chesslogic.game

import cats.data.NonEmptyList
import chesslogic.White
import chesslogic.board.{Board, Move, Position}
import chesslogic.game.Game.Turn
import chesslogic.game.Game.Turn.{BlackTurn, WhiteTurn}

case class Game(gameHistory:NonEmptyList[Board] = NonEmptyList.one(Board()), turn : Turn = WhiteTurn) {

  val currentBoard: Board = gameHistory.head


  def makeMove(from:Position,to:Position):Option[Game] = {
    val currentBoard = gameHistory.head
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      possibleMoves <- possibleMovesOption
      tileToMove <- currentBoard.getTile(to) if possibleMoves.contains(to)
      tileFrom <- currentBoard.getTile(from)
      attackingPiece <- tileFrom.currentPiece
      move = Move(tileFrom,tileToMove)
      isColorCorrect = if(attackingPiece.color == White) turn == WhiteTurn else turn == BlackTurn
      newBoard <- currentBoard.getBoardAfterMove(move,currentBoard) if isColorCorrect
    } yield Game(newBoard :: this.gameHistory,turn = turn.changeTurn)
  }
  


  def makeMoveWithoutTurn(from:Position,to:Position):Option[Game] = {
    val currentBoard = gameHistory.head
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      possibleMoves <- possibleMovesOption
      tileToMove <- currentBoard.getTile(to) if possibleMoves.contains(to)
      tileFrom <- currentBoard.getTile(from)
      move = Move(tileFrom,tileToMove)
      newBoard <- currentBoard.getBoardAfterMove(move,currentBoard)
    } yield Game(newBoard :: this.gameHistory,turn = turn.changeTurn)

  }

}

object Game {
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
