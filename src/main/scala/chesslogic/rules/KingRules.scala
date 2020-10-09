package chesslogic.rules

import chesslogic.board.{Board, Position}
import chesslogic.pieces.King
import chesslogic.rules.CheckAndMateRules.isKingChecked
import chesslogic.rules.RulesForKingAndKnight.getAllMoves

object KingRules extends MovingRules[King] {
  private val combinations = (for {
    x <- -1 to 1
    y <- -1 to 1
  } yield (x, y)).distinct.toList


  override def getPossibleMoves(position: Position, board: Board): List[Position] = {
    val castlingMoves = List(
      castlingMove(position,board,isLeftRook = true),
      castlingMove(position,board,isLeftRook = false)).collect {
      case Some(position) => position
    }

    getAllMoves(position, board,combinations)._1 ++ castlingMoves
  }

  override def getPossibleAttacks(position: Position, board: Board): List[Position] =
    getAllMoves(position, board,combinations)._2



  private def castlingMove(position: Position,board: Board,isLeftRook:Boolean):Option[Position] = {
    val rookColumn = if(isLeftRook) 1 else 8
    val newKingPositionColumn = if(isLeftRook) 2 else 7
    for {
      kingTile <- board.getTile(position) if !kingTile.hasMoved
      kingPosition = kingTile.position
      rookPosition = Position(kingPosition.row,rookColumn)
      rookTile <- getRookTileOption(kingPosition,board,rookPosition.column)
        if(areTilesClear(kingPosition,rookTile.position,board) &&
          !isKingChecked(board,kingTile.currentPiece.get.color))
      newKingPosition = Position(rookTile.position.row,newKingPositionColumn)
    } yield newKingPosition
  }

  private def areTilesClear(from: Position,to:Position,board: Board):Boolean = {
    val tiles = board.tiles.values.filter(tile => {
      val p = tile.position
      p.row == from.row && isBetween(p.column,from,to)
    })
    tiles.forall(!_.hasPiece)
  }

  private def isBetween(column:Int,from:Position,to:Position):Boolean = {
    val smaller = Math.min(from.column,to.column)
    val bigger = Math.max(from.column,to.column)
    (smaller + 1 until bigger).contains(column)
  }


  private def getRookTileOption(position: Position,board: Board,column:Int) =
  for {
  tile <- board.getTile(position)
  rookPosition = Position(tile.position.row,column)
    rookTile <- board.getTile(rookPosition) if (!rookTile.hasMoved)
  } yield rookTile


}
