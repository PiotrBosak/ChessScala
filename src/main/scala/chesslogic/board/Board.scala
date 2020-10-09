package chesslogic.board

import chesslogic.pieces.{Bishop, King, Knight, Pawn, Piece, Queen, Rook}
import chesslogic.rules.{BishopRules, CheckAndMateRules, KingRules, KnightRules, PawnRules, QueenRules, RookRules}

case class Board private (tiles:Map[Position, Tile]) {

  def getTile(position: Position):Option[Tile] = tiles.get(position)

  def changeTile(tile:Tile):Board ={
    val newTiles = tiles + (tile.position -> tile)
    Board(newTiles)
  }
   def getPossibleMoves(position: Position):Option[List[Position]] = for {
    tile <- getTile(position)
    piece <- tile.currentPiece
    possibleMoves = getMovesForPiece(piece,position)
  } yield possibleMoves


  def getMovesForPiece(piece: Piece,position: Position): List[Position] = {
    piece match {
      case Pawn(_) => PawnRules.getPossibleMoves(position,this) ++ PawnRules.getPossibleAttacks(position,this)
      case Bishop(_) => BishopRules.getPossibleMoves(position, this) ++ BishopRules.getPossibleAttacks(position, this)
      case Rook(_) => RookRules.getPossibleMoves(position,this) ++ RookRules.getPossibleAttacks(position, this)
      case Queen(_) => QueenRules.getPossibleMoves(position, this) ++ QueenRules.getPossibleAttacks(position, this)
      case Knight(_) => KnightRules.getPossibleMoves(position, this) ++ KnightRules.getPossibleAttacks(position, this)
      case King(_) => KingRules.getPossibleMoves(position, this) ++ KingRules.getPossibleAttacks(position, this)
    }
  }

  def getBoardAfterMove(tileFrom:Tile,tileTo:Tile):Option[Board] = {
     tileFrom.currentPiece.map(piece => {
       (piece,
         if (isCastling(tileFrom, tileTo)) makeCastlingMove(tileFrom, tileTo)
        else makeNormalMove(tileFrom, tileTo,piece))
     }).filter(tuple => !CheckAndMateRules.isKingChecked(tuple._2,tuple._1.color)).map(_._2)

  }
  private def makeCastlingMove(tileFrom: Tile, tileTo: Tile):Board = {
    val kingNewColumn = tileTo.position.column
    val rookOldColumn = if(kingNewColumn == 2) 1 else 8
    val rookNewColumn = if(kingNewColumn == 2) 3 else 6

   val gameOption =  for {
      oldRookTile <- getTile(Position(tileFrom.position.row,rookOldColumn))
      tileAfterKing = tileFrom.copy(currentPiece = None)
      kingPiece <- tileFrom.currentPiece
      rookPiece <- oldRookTile.currentPiece
      newKingTile = tileTo.copy(currentPiece = Some(kingPiece),hasMoved = true)
      tileAfterRook = oldRookTile.copy(currentPiece = None)
      newRookPosition = Position(tileFrom.position.row,rookNewColumn)
      tileForRook <- getTile(newRookPosition)
      newRookTile = tileForRook.copy(currentPiece = Some(rookPiece))
    } yield changeTile(tileAfterKing).changeTile(newKingTile).changeTile(tileAfterRook).changeTile(newRookTile)

    gameOption.getOrElse(throw new RuntimeException("this should never happen!!!"))

  }

  private def isCastling(tileFrom: Tile, tileTo: Tile):Boolean = {
    tileFrom.currentPiece.exists {
      case _: King => Math.abs(tileFrom.position.column - tileTo.position.column) >= 2
      case _: Piece => false
    }

  }

    private def makeNormalMove(tileFrom: Tile, tileTo: Tile,pieceMoving:Piece):Board ={
       val newTileFrom = tileFrom.copy(currentPiece = None,hasMoved = true)
        val newTileTo = tileTo.copy(currentPiece = Some(pieceMoving),hasMoved = true)
      changeTile(newTileFrom).changeTile(newTileTo)
    }




}
object Board {
  def apply(): Board = Board(BoardFactory())
}
