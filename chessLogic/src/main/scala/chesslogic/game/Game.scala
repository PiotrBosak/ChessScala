package chesslogic.game

import chesslogic.White
import chesslogic.board.{Board, Move, Position}

case class Game(gameHistory:List[Board] = List(Board()), isWhiteTurn:Boolean = true) {

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
      isColorCorrect = if(attackingPiece.color == White) isWhiteTurn else !isWhiteTurn
      newBoard <- currentBoard.getBoardAfterMove(move,currentBoard) if isColorCorrect
    } yield Game(newBoard :: this.gameHistory,isWhiteTurn = !this.isWhiteTurn)
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
    } yield Game(newBoard :: this.gameHistory,isWhiteTurn = !this.isWhiteTurn)

  }









}
