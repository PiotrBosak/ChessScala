package chesslogic.rules

import chesslogic.{Black, Color, White}
import chesslogic.board.{Board, Position, Tile}

object PawnRules extends MovingRules{
  override def getPossibleAttacks(position: Position,board: Board): List[Position] = {
    val l = List(getLeftAttack(position,board),getRightAttack(position,board))
    l.filter(_.isDefined).map(_.get)
  }

  override def getPossibleMoves(position: Position,board: Board): List[Position] = {
    val l = List(oneTileMove(position,board),twoTileMove(position,board))
    l.filter(_.isDefined).map(_.get.position)
  }


  private def getRightAttack(position: Position,board: Board):Option[Position] = {
    val pieceTileOption = board.getTile(position)
    for {
      tile <- pieceTileOption
      piece <- tile.currentPiece
      rightAttackTile <- getRightAttackTile(position,board,piece.color)
      attackedPiece <- rightAttackTile.currentPiece if attackedPiece.color != piece.color
    } yield rightAttackTile.position
  }
  private def getRightAttackTile(position: Position, board: Board, color: Color):Option[Tile] = {
    color match {
      case White => board.getTile(Position(position.row+1,position.column+1))
      case Black =>board.getTile(Position(position.row-1,position.column+1))
    }

  }

  private def getLeftAttack(position: Position,board: Board):Option[Position] = {
    val pieceTileOption = board.getTile(position)
    for {
      tile <- pieceTileOption
      piece <- tile.currentPiece
      leftAttackTile <- getLeftAttackTile(position,board,piece.color)
      attackedPiece <- leftAttackTile.currentPiece if attackedPiece.color != piece.color
    } yield leftAttackTile.position
  }
  private def getLeftAttackTile(position: Position, board: Board, color: Color):Option[Tile] = {
    color match {
      case White => board.getTile(Position(position.row+1,position.column-1))
      case Black =>board.getTile(Position(position.row-1,position.column-1))
    }

  }

  private def oneTileMove(position: Position,board: Board):Option[Tile] = {
    val pieceTileOption = board.getTile(position)
    for {
      tile <- pieceTileOption
      piece <- tile.currentPiece
      color = piece.color
      nextTile <- getNextPawnTile(tile.position,color,board,1) if nextTile.currentPiece.isEmpty
    } yield nextTile

  }

  private def getNextPawnTile(position: Position,pieceColor:Color,board: Board,amount:Int):Option[Tile] = pieceColor match {
      case White => board.getTile(position.copy(row = position.row+amount))
      case Black =>board.getTile(position.copy(row = position.row-amount))
    }

  private def twoTileMove(position: Position,board: Board):Option[Tile] = {
    val pieceTileOption = board.getTile(position)
    for {
      tile <- pieceTileOption if !tile.hasMoved
      piece <- tile.currentPiece
      color = piece.color
      nextTile <- getNextPawnTile(tile.position, color, board, 2) if nextTile.currentPiece.isEmpty
    } yield nextTile
  }


}
