package chesslogic.board

import chesslogic.White
import chesslogic.board.position.{Move, MoveType, Position, Rank}
import chesslogic.pieces._
import chesslogic.rules._
import chesslogic.board.position.File._
import chesslogic.board.position.Rank._
import chesslogic.board.position.MoveType._
import chesslogic.utils.orphanInstances._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive


@derive(encoder,decoder)
case class Board private (tiles:Map[Position, Tile],previousMove:Option[Move]) {


  def getTile(position: Position): Tile = findTile(position)


  def updateBoard(tile: Tile, move: Move): Board = {
    val newTiles = tiles + (tile.position -> tile)
    Board(newTiles, Some(move))
  }

  def findTile(position: Position): Tile =
    tiles.getOrElse(position, throw new RuntimeException(""))

  def getPossibleMoves(position: Position): Option[List[(MoveType, Position)]] = for {
    _ <- Option(())
    tile = getTile(position)
    piece <- tile.currentPiece
    possibleMoves = getMovesForPiece(piece, position)
  } yield possibleMoves

  def getMovesForPiece(piece: Piece, position: Position): List[(MoveType, Position)] = {
    piece match {
      case Pawn(_)   => PawnRules.getPossibleMoves(position, this) ++ PawnRules.getPossibleAttacks(position, this)
      case Bishop(_) => BishopRules.getPossibleMoves(position, this) ++ BishopRules.getPossibleAttacks(position, this)
      case Rook(_)   => RookRules.getPossibleMoves(position, this) ++ RookRules.getPossibleAttacks(position, this)
      case Queen(_)  => QueenRules.getPossibleMoves(position, this) ++ QueenRules.getPossibleAttacks(position, this)
      case Knight(_) => KnightRules.getPossibleMoves(position, this) ++ KnightRules.getPossibleAttacks(position, this)
      case King(_)   => KingRules.getPossibleMoves(position, this) ++ KingRules.getPossibleAttacks(position, this)
    }
  }

  def getBoardAfterMove(moveType: MoveType, tileFrom: Tile, tileTo: Tile, board: Board): Option[Board] =
    tileFrom.currentPiece
      .map(piece => {
        (
          piece,
          moveType match {
            case Castling => makeCastlingMove(tileFrom, tileTo)
            case LePassant => makeLePassant(tileFrom, tileTo, piece, board)
            case _ => makeNormalMove(tileFrom, tileTo, piece)
          }
        )

      })
      .filter(tuple => !CheckAndMateRules.isKingChecked(tuple._2, tuple._1.color))
      .map(_._2)


  private def makeLePassant(tileFrom: Tile, tileTo: Tile, piece: Piece, board: Board): Board = {
    val attackingColor = tileFrom.currentPiece.get.color
    val difference     = if (attackingColor == White) 1 else -1
    val newTileFrom    = tileFrom.copy(currentPiece = None, hasMoved = true)
    val newTileTo      = tileTo.copy(currentPiece = Some(piece), hasMoved = true)
    val capturedPawntile =
      board.getTile(tileTo.position.copy(rank = Rank.fromIntUnsafe(tileTo.position.rank.toNumber - difference)))
    val newTileAfterCapturing = capturedPawntile.copy(currentPiece = None)
    val newMove = Move(tileFrom.position, newTileTo.position, LePassant)
    updateBoard(newTileFrom, newMove)
      .updateBoard(newTileTo, newMove)
      .updateBoard(newTileAfterCapturing, newMove)

  }

  private def makeCastlingMove(tileFrom: Tile, tileTo: Tile): Board = {
    val kingNewFile = tileTo.position.file
    val (rookOldFile, rookNewFile) =
      if (kingNewFile == B) (A, C) else (H, F)

    val gameOption = for {
      _ <- Option(())
      oldRookTile = getTile(Position(rookOldFile, tileFrom.position.rank))
      tileAfterKing = tileFrom.copy(currentPiece = None)
      kingPiece <- tileFrom.currentPiece
      rookPiece <- oldRookTile.currentPiece
      newKingTile = tileTo.copy(currentPiece = Some(kingPiece), hasMoved = true)
      tileAfterRook = oldRookTile.copy(currentPiece = None)
      newRookPosition = Position(rookNewFile, tileFrom.position.rank)
      tileForRook = getTile(newRookPosition)
      newRookTile = tileForRook.copy(currentPiece = Some(rookPiece))
      newMove = Move(tileFrom.position, newKingTile.position, Castling)
    } yield updateBoard(tileAfterKing, newMove)
      .updateBoard(newKingTile, newMove)
      .updateBoard(tileAfterRook, newMove)
      .updateBoard(newRookTile, newMove)

    gameOption.getOrElse(throw new RuntimeException("this should never happen!!!"))
  }

  private def makeNormalMove(tileFrom: Tile, tileTo: Tile, pieceMoving: Piece): Board = {
    val newTileFrom = tileFrom.copy(currentPiece = None, hasMoved = true)
    val newTileTo = tileTo.copy(currentPiece = Some(pieceMoving), hasMoved = true)
    val newMove = Move(tileFrom.position, newTileTo.position, Normal)
    updateBoard(newTileFrom, newMove).updateBoard(newTileTo, newMove)
  }
}

object Board {
  def apply(): Board = new Board(BoardFactory(), None)
}
