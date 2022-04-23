package chesslogic.rules

import chesslogic.Color
import chesslogic.board.position.{File, Position, Rank}
import chesslogic.board.{Board, Tile}

import scala.annotation.tailrec

 protected object RulesForMultiTileMoves {


   private final case class InnerPosition(row: Int, column: Int) {
     def getTile(board: Board): Option[Tile] =
       if (row > 8 || row < 1 || column > 8 || column < 1) None
       else Some(board.findTile(Position(File.fromIntUnsafe(column), Rank.fromIntUnsafe(row))))
   }

   def getViablePositionsForMoves(
                                   board: Board,
                                   position: Position,
                                   nextRowDifference: Int,
                                   nextColumnDifference: Int,
                                   currentPositions: List[Position] = List.empty[Position]
                                 ): List[Position] = {

     @tailrec
     def aux(
              board: Board,
              position: InnerPosition,
              nextRowDifference: Int,
              nextColumnDifference: Int,
              currentPositions: List[InnerPosition]
            ): List[InnerPosition] = {
       val nextPosition = InnerPosition(position.row + nextRowDifference, position.column + nextColumnDifference)
       nextPosition.getTile(board) match {
         case Some(tile) =>
           if (tile.hasPiece) currentPositions
           else {
             aux(
               board,
               nextPosition,
               nextRowDifference,
               nextColumnDifference,
               nextPosition :: currentPositions
             )
           }
         case None => currentPositions
       }
     }

     aux(
       board,
       InnerPosition(position.rank.toNumber, position.file.toNumber),
       nextRowDifference,
       nextColumnDifference,
       currentPositions.map(p => InnerPosition(p.rank.toNumber, p.file.toNumber))
     ).map(p => Position(File.fromIntUnsafe(p.column), Rank.fromIntUnsafe(p.row)))
   }

   def getViablePositionForAttacks(
                                    board: Board,
                                    position: Position,
                                    nextRowDifference: Int,
                                    nextColumnDifference: Int,
                                    attackingColor: Color
                                  ): Option[Position] = {

     @tailrec
     def aux(
              board: Board,
              position: InnerPosition,
              nextRowDifference: Int,
              nextColumnDifference: Int,
              attackingColor: Color
            ): Option[InnerPosition] = {
       val nextPosition = InnerPosition(position.row + nextRowDifference, position.column + nextColumnDifference)
       nextPosition.getTile(board) match {
         case Some(tile) =>
           tile.currentPiece match {
             case Some(piece) =>
               if (piece.color != attackingColor) Some(nextPosition)
               else None
             case None =>
               aux(board, nextPosition, nextRowDifference, nextColumnDifference, attackingColor)
           }
         case None => None

       }
     }
     aux(
       board,
       InnerPosition(position.rank.toNumber, position.file.toNumber),
       nextRowDifference,
       nextColumnDifference,
       attackingColor
     ).map(p => Position(File.fromIntUnsafe(p.column), Rank.fromIntUnsafe(p.row)))
   }

 }
