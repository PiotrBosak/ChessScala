package chesslogic.board

import chesslogic.White
import chesslogic.pieces.{Bishop, King, Knight, Pawn, Piece, Queen, Rook}
import chesslogic.rules.{BishopRules, CheckAndMateRules, KingRules, KnightRules, PawnRules, QueenRules, RookRules}

case class Board private (tiles:Map[Position, Tile],previousMove:Option[Move]) {



  def getTile(position: Position):Option[Tile] = tiles.get(position)

  def updateBoard(tile:Tile,move: Move):Board ={
    val newTiles = tiles + (tile.position -> tile)
    Board(newTiles,Some(move))
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

  def getBoardAfterMove(move: Move,board: Board):Option[Board] = {
     move.from.currentPiece.map(piece => {
       (piece,
         if (isCastling(move.from, move.to)) makeCastlingMove(move)
         else if(isLePassant(move)) makeLePassant(move,piece,board)
        else makeNormalMove(move,piece))
     }).filter(tuple => !CheckAndMateRules.isKingChecked(tuple._2,tuple._1.color)).map(_._2)

  }

  private def isLePassant(move: Move):Boolean = {
    previousMove match {
      case Some(previous) =>
        isTwoTilePawnMove(previous) &&
        move.to.position.column == previous.to.position.column &&
          Math.abs(move.to.position.row - previous.to.position.row) == 1
      case None =>false
    }
  }

  private def isTwoTilePawnMove(move: Move):Boolean = {
    val rowDifference = move.from.position.row - move.to.position.row
    Math.abs(rowDifference) == 2 && (move.from.currentPiece match {
      case Some(piece) => piece.isInstanceOf[Pawn]
      case None => false
    })
  }


  private def makeLePassant(move: Move,piece: Piece,board: Board):Board = {
    val attackingColor = move.from.currentPiece.get.color
    val difference = if(attackingColor == White) 1 else -1
    val newTileFrom = move.from.copy(currentPiece = None,hasMoved = true)
    val newTileTo = move.to.copy(currentPiece = Some(piece),hasMoved = true)
    val capturedPawnTileOption = board.getTile(move.to.position.copy(row = move.to.position.row - difference))
    val newTileAfterCapturing = capturedPawnTileOption.get.copy(currentPiece = None)
    val newMove = Move(move.from,newTileTo)
    updateBoard(newTileFrom,newMove).updateBoard(newTileTo,newMove).updateBoard(newTileAfterCapturing,newMove)

  }
  private def makeCastlingMove(move: Move):Board = {
    val kingNewColumn = move.to.position.column
    val rookOldColumn = if(kingNewColumn == 2) 1 else 8
    val rookNewColumn = if(kingNewColumn == 2) 3 else 6

   val gameOption =  for {
      oldRookTile <- getTile(Position(move.from.position.row,rookOldColumn))
      tileAfterKing = move.from.copy(currentPiece = None)
      kingPiece <- move.from.currentPiece
      rookPiece <- oldRookTile.currentPiece
      newKingTile = move.to.copy(currentPiece = Some(kingPiece),hasMoved = true)
      tileAfterRook = oldRookTile.copy(currentPiece = None)
      newRookPosition = Position(move.from.position.row,rookNewColumn)
      tileForRook <- getTile(newRookPosition)
      newRookTile = tileForRook.copy(currentPiece = Some(rookPiece))
      newMove = Move(move.from,newKingTile)
    } yield updateBoard(tileAfterKing,newMove).updateBoard(newKingTile,newMove).updateBoard(tileAfterRook,newMove).updateBoard(newRookTile,newMove)

    gameOption.getOrElse(throw new RuntimeException("this should never happen!!!"))

  }

  private def isCastling(tileFrom: Tile, tileTo: Tile):Boolean = {
    tileFrom.currentPiece.exists {
      case _: King => Math.abs(tileFrom.position.column - tileTo.position.column) >= 2
      case _: Piece => false
    }

  }

    private def makeNormalMove(move: Move,pieceMoving:Piece):Board ={
       val newTileFrom = move.from.copy(currentPiece = None,hasMoved = true)
        val newTileTo = move.to.copy(currentPiece = Some(pieceMoving),hasMoved = true)
      val newMove = Move(move.from,newTileTo)
      updateBoard(newTileFrom,newMove).updateBoard(newTileTo,newMove)
    }




}
object Board {
  def apply(): Board = Board(BoardFactory(),None)
}
