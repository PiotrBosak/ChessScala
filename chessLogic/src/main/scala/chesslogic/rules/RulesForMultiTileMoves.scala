package chesslogic.rules

import chesslogic.Color
import chesslogic.board.{Board, Position}

import scala.annotation.tailrec

 protected object RulesForMultiTileMoves {

  @tailrec
   def getViablePositionsForMoves(board: Board, position: Position,
                                         nextRowDifference: Int,
                                         nextColumnDifference: Int,
                                         currentPositions: List[Position] = List.empty[Position]): List[Position] = {
    val nextPosition = Position(position.row + nextRowDifference, position.column + nextColumnDifference)
    board.getTile(nextPosition) match {
      case Some(tile) =>
        if (tile.hasPiece) currentPositions
        else {
          getViablePositionsForMoves(board, nextPosition, nextRowDifference, nextColumnDifference, nextPosition :: currentPositions)
        }
      case None => currentPositions
    }

  }

  @tailrec
   def getViablePositionForAttacks(board: Board,
                                          position: Position,
                                          nextRowDifference: Int,
                                          nextColumnDifference: Int,
                                          attackingColor: Color): Option[Position] = {
    val nextPosition = Position(position.row + nextRowDifference, position.column + nextColumnDifference)
    board.getTile(nextPosition) match {
      case Some(tile) =>
        tile.currentPiece match {
          case Some(piece) =>
            if (piece.color != attackingColor) Some(nextPosition)
            else None
          case None =>
            getViablePositionForAttacks(board, nextPosition, nextRowDifference, nextColumnDifference, attackingColor)
        }
      case None => None

    }


  }

}
