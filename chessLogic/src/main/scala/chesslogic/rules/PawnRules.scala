package chesslogic.rules

import chesslogic.{Black, Color, White}
import chesslogic.board.{Board, Move, Position, Tile}
import chesslogic.pieces.Pawn

object PawnRules extends MovingRules[Pawn]{
  override def getPossibleAttacks(position: Position,board: Board): List[Position] = {
    List(getLeftAttack(position,board),
      getRightAttack(position,board),
      lePassant(position,board))
    .collect { case Some(position) => position }
  }

  override def getPossibleMoves(position: Position,board: Board): List[Position] = {
    List(oneTileMove(position,board),twoTileMove(position,board))
      .collect { case Some(tile) => tile.position }
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

  private def lePassant(position: Position,board: Board):Option[Position] = {
    val first = lePassantGet(position,board,isLeft = true)
    val second = lePassantGet(position,board,isLeft = false)

    first match {
      case Some(value) => Some(value)
      case None =>second
    }

  }

  private def lePassantGet(position: Position,board: Board,isLeft:Boolean):Option[Position] = {
    val difference = if(isLeft) -1 else 1
    for {
      tile <- board.getTile(position)
      piece <- tile.currentPiece
      attackingColor = piece.color
      lastMove <- board.previousMove

      positionToLeft = position.copy(column = position.column+difference) if checkTileForLePassant(lastMove,attackingColor,positionToLeft)
      rowDifference = if(attackingColor == White) 1 else -1
      positionToMove = positionToLeft.copy(row = positionToLeft.row + rowDifference)
    } yield positionToMove
  }

  private def checkTileForLePassant(move:Move,attackingColor:Color,attackingTilePosition:Position):Boolean = {
    val startingEnemyRow = if(attackingColor == White) 7 else 2
    val isRowTheSame = move.to.position.row == attackingTilePosition.row
    val isPreviousStartCorrect = move.from.position.row == startingEnemyRow
    val isColumnTheSame = move.to.position.column == attackingTilePosition.column

    val isEnemyPawn = move.from.currentPiece match {
      case Some(piece) => piece.isInstanceOf[Pawn] && piece.color != attackingColor
      case None =>false
    }
    isRowTheSame && isPreviousStartCorrect && isEnemyPawn && isColumnTheSame

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
