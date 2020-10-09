package chesslogic.game

import chesslogic.board.{Board, Position}

case class Game(gameHistory:List[Board] = List(Board()), isWhiteTurn:Boolean = true) {

  val currentBoard: Board = gameHistory.head


  def makeMoveWithoutTurn(from:Position, to:Position):Option[Game] = {
    val currentBoard = gameHistory.head
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      possibleMoves <- possibleMovesOption
      tileToMove <- currentBoard.getTile(to) if possibleMoves.contains(to)
      tileFrom <- currentBoard.getTile(from)
      newBoard <- currentBoard.getBoardAfterMove(tileFrom,tileToMove)
    } yield Game(newBoard :: this.gameHistory,isWhiteTurn = !this.isWhiteTurn)

  }






}
