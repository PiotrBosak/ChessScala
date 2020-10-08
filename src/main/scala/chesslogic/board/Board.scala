package chesslogic.board

import chesslogic.pieces.{Bishop, King, Knight, Pawn, Piece, Queen, Rook}
import chesslogic.rules.{BishopRules, KingRules, KnightRules, PawnRules, QueenRules, RookRules}

case class Board private (tiles:Map[Position, Tile]) {

  def getTile(position: Position):Option[Tile] = tiles.get(position)

  def changeTile(tile:Tile):Board ={
    val newTiles = tiles + (tile.position -> tile)
    Board(newTiles)
  }


}
object Board {
  def apply(): Board = Board(BoardFactory())

  def getMovesForPiece(piece: Piece,board:Board,position: Position): List[Position] = {
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
