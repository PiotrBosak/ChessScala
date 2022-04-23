package chesslogic.game

import chesslogic.White
import chesslogic.board.Board
import cats.implicits._
import chesslogic.board.position.Position
import chesslogic.game.FullGame.Turn
import chesslogic.game.FullGame.Turn.{BlackTurn, WhiteTurn}
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

@derive(encoder,decoder)
case class SimpleGame(currentBoard: Board, turn: Turn = WhiteTurn) {

  def makeMove(movingPlayer: Player, from: Position, to: Position): Option[SimpleGame] = {
    val possibleMovesOption = currentBoard.getPossibleMoves(from)
    for {
      _               <- validMover(movingPlayer).guard[Option]
      possibleMoves   <- possibleMovesOption
      (moveType, pos) <- possibleMoves.find(p => p._2 == to)
      tileToMove = currentBoard.getTile(to)
      tileFrom   = currentBoard.getTile(from)
      attackingPiece <- tileFrom.currentPiece
      isColorCorrect = if (attackingPiece.color == White) turn == WhiteTurn else turn == BlackTurn
      newBoard <- currentBoard.getBoardAfterMove(moveType, tileFrom, currentBoard.findTile(to), currentBoard)
      if isColorCorrect
    } yield SimpleGame(newBoard, turn = turn.changeTurn)
  }

  private def validMover(player: Player) = {
    player match {
      case WhitePlayer => turn == WhiteTurn
      case BlackPlayer => turn == BlackTurn
    }
  }


}
