package chesslogic.game

import chesslogic.board.Board.getMovesForPiece
import chesslogic.board.{Board, BoardFactory, Position}
import chesslogic.pieces._
import chesslogic.rules.{BishopRules, CheckAndMateRules, KingRules, KnightRules, PawnRules, QueenRules, RookRules}

case class Game(gameHistory:List[Board] = List(Board()), isWhiteTurn:Boolean = true) {

  val currentBoard: Board = gameHistory.head


  def makeMoveWithoutTurn(from:Position, to:Position):Option[Game] = {
    val currentBoard = gameHistory.head
    val possibleMovesOption = getPossibleMoves(from,currentBoard)
    for {
      possibleMoves <- possibleMovesOption
      tileToMove <- currentBoard.getTile(to) if possibleMoves.contains(to)
      tileFrom <- currentBoard.getTile(from)
      pieceMoving <- tileFrom.currentPiece
      newTileFrom = tileFrom.copy(currentPiece = None,hasMoved = true)
      newTileTo = tileToMove.copy(currentPiece = Some(pieceMoving),hasMoved = true)
      newBoard = currentBoard.changeTile(newTileFrom).changeTile(newTileTo)
        if !CheckAndMateRules.isKingChecked(newBoard,pieceMoving.color)
    } yield Game(newBoard :: this.gameHistory,isWhiteTurn = !this.isWhiteTurn)

  }

  private def getPossibleMoves(position: Position, board: Board):Option[List[Position]] = for {
      tile <- board.getTile(position)
      piece <- tile.currentPiece
      possibleMoves = getMovesForPiece(piece,board,position)
    } yield possibleMoves





}
