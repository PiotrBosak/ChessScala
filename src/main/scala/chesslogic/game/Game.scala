package chesslogic.game

import chesslogic.board.{Board, BoardFactory, Position}
import chesslogic.pieces._
import chesslogic.rules.{BishopRules, KingRules, KnightRules, PawnRules, QueenRules, RookRules}

case class Game(gameHistory:List[Board] = List(Board())) {

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
      newBoard = currentBoard.changeTile(newTileFrom).changeTile(newTileTo) if(checkBoardAfterMove(newBoard))
    } yield Game(newBoard :: this.gameHistory)

  }

  private def checkBoardAfterMove(board: Board):Boolean = {
    //todo check whether there is no checking after making move
    true
  }

  def makeAttack(position: Position):Either[String,Game] = {
    null
  }

  private def getPossibleMoves(position: Position, board: Board):Option[List[Position]] = for {
      tile <- board.getTile(position)
      piece <- tile.currentPiece
      possibleMoves = getMovesForPiece(piece,board,position)
    } yield possibleMoves



  private def getMovesForPiece(piece: Piece,board:Board,position: Position) = {
    piece match {
      case Pawn(_) => PawnRules.getPossibleMoves(position,board) ++ PawnRules.getPossibleAttacks(position,board)
      case Bishop(_) => BishopRules.getPossibleMoves(position, board) ++ BishopRules.getPossibleAttacks(position, board)
      case Rook(_) => RookRules.getPossibleMoves(position,board) ++ RookRules.getPossibleAttacks(position, board)
      case Queen(_) => QueenRules.getPossibleMoves(position, board) ++ QueenRules.getPossibleAttacks(position, board)
      case Knight(_) => KnightRules.getPossibleMoves(position, board) ++ KnightRules.getPossibleAttacks(position, board)
      case King(_) => KingRules.getPossibleMoves(position, board) ++ KingRules.getPossibleAttacks(position, board)
    }
  }

}
