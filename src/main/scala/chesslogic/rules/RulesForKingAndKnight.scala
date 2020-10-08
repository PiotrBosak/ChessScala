package chesslogic.rules

import chesslogic.board.{Board, Position}

object RulesForKingAndKnight {

  def getAllMoves(position: Position, board: Board, combinations: List[(Int, Int)]): (List[Position], List[Position]) = {
    board.getTile(position) match {
      case Some(tile) =>
        val possiblePositions = combinations
          .map(t => Position(position.row + t._1, position.column + t._2))
          .map(board.getTile)
          .filter(_.isDefined)
          .map(_.get)
        val attackingPiece = tile.currentPiece.get
        val moves = possiblePositions.filter(!_.hasPiece).map(_.position)
        val attacks = possiblePositions.foldLeft(List.empty[Position])((acc, t) => {
          t.currentPiece match {
            case Some(piece) =>
              if (piece.color != attackingPiece.color) t.position :: acc
              else acc
            case None => acc
          }
        })
        (moves, attacks)
      case None => (Nil, Nil)
    }

  }
}
