package lib.logic.game

import lib.logic.Color.*
import FullGame.Turn
import FullGame.Turn.{ BlackTurn, WhiteTurn }
import cats.syntax.all.*
import lib.logic.board.{Board, Move, Position}
import io.circe.Codec
import lib.domain.auth.UserId
import lib.domain.game.GameId

  final case class PvPGame(
      whitePlayer: UserId,
      blackPlayer: UserId,
      gameId: GameId,
      simpleGame: SimpleGame
  ) derives Codec.AsObject


case class SimpleGame(currentBoard: Board, turn: Turn = WhiteTurn) derives Codec.AsObject {


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
      newBoard <- currentBoard.getBoardAfterMove(moveType, tileFrom, currentBoard.findTile(to))
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
