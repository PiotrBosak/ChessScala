package chesslogic.board

import chesslogic._
import board.BoardFactory.{createTileWithBlackKing, createTileWithBlackQueen, createTileWithWhiteKing, createTileWithWhiteQueen, createTilesWithBlackBishops, createTilesWithBlackKnights, createTilesWithBlackPawns, createTilesWithBlackRooks, createTilesWithNoPieces, createTilesWithWhiteBishops, createTilesWithWhiteKnights, createTilesWithWhitePawns, createTilesWithWhiteRooks}
import chesslogic.pieces._

import scala.annotation.tailrec

case object BoardFactory {
  def apply(): Map[Position, Tile] = {

    createTilesWithBlackPawns() ++
      createTilesWithWhiteRooks() ++
      createTilesWithBlackRooks() ++
      createTilesWithWhiteKnights() ++
      createTilesWithBlackKnights() ++
      createTilesWithWhiteBishops() +
      createTileWithBlackKing() ++
      createTilesWithNoPieces() ++
      createTilesWithWhitePawns() ++
      createTilesWithBlackBishops() +
      createTileWithWhiteQueen() +
      createTileWithBlackQueen() +
      createTileWithWhiteKing()

  }

  private def createTilesWithBlackBishops(): Map[Position, Tile] = Map(
    Position(8, 3) -> Tile(White, Position(8, 3), Some(Bishop(Black))),
    Position(8, 6) -> Tile(Black, Position(8, 6), Some(Bishop(Black)))
  ).view.mapValues(_.get).toMap


  private def createTilesWithBlackKnights(): Map[Position, Tile] = Map(
    Position(8, 2) -> Tile(Black, Position(8, 2), Some(Knight(Black))),
    Position(8, 7) -> Tile(White, Position(8, 7), Some(Knight(Black)))
  ).view.mapValues(_.get).toMap

  private def createTilesWithBlackRooks(): Map[Position, Tile] = Map(
    Position(8, 1) -> Tile(White, Position(8, 1), Some(Rook(Black))),
    Position(8, 8) -> Tile(Black, Position(8, 8), Some(Rook(Black)))
  ).view.mapValues(_.get).toMap

  private def createTilesWithWhiteBishops(): Map[Position, Tile] = Map(
    Position(1, 3) -> Tile(Black, Position(1, 3), Some(Bishop(White))),
    Position(1, 6) -> Tile(White, Position(1, 6), Some(Bishop(White)))
  ).view.mapValues(_.get).toMap

  private def createTilesWithWhiteKnights(): Map[Position, Tile] = Map(
    Position(1, 2) -> Tile(White, Position(1, 2), Some(Knight(White))),
    Position(1, 7) -> Tile(Black, Position(1, 7), Some(Knight(White)))
  ).view.mapValues(_.get).toMap

  private def createTilesWithWhiteRooks(): Map[Position, Tile] = Map(
    Position(1, 1) -> Tile(Black, Position(1, 1), Some(Rook(White))),
    Position(1, 8) -> Tile(White, Position(1, 8), Some(Rook(White)))
  ).view.mapValues(_.get).toMap

  private def createTileWithBlackKing(): (Position, Tile) =
    Position(8, 5) -> Tile(White, Position(8, 5), Some(King(Black))).get

  private def createTileWithBlackQueen(): (Position, Tile) =
    Position(8, 4) -> Tile(Black, Position(8, 4), Some(Queen(Black))).get

  private def createTileWithWhiteKing(): (Position, Tile) =
    Position(1, 5) -> Tile(Black, Position(1, 5), Some(King(White))).get

  private def createTileWithWhiteQueen(): (Position, Tile) =
    Position(1, 4) -> Tile(White, Position(1, 4), Some(Queen(White))).get


  private def createTilesWithNoPieces(): Map[Position, Tile] = {
    @tailrec
    def aux(row: Int, map: Map[Position, Tile]): Map[Position, Tile] =
      if (row > 6) map
      else aux(row + 1, map ++ createTiles(row, None))

    aux(3, Map())

  }


  private def createTilesWithBlackPawns(): Map[Position, Tile] =
    createTiles(7, Some(Pawn(Black)))

  private def createTilesWithWhitePawns(): Map[Position, Tile] =
    createTiles(2, Some(Pawn(White)))


  private def createTiles(row: Int, piece: Option[Piece]) = {
    @tailrec
    def createTilesInColumn(column: Int, map: Map[Position, Tile]): Map[Position, Tile] = {
      if (column > 8) map
      else if ((row + column) % 2 == 0)
        createTilesInColumn(column + 1, map + (Position(row, column) -> Tile(Black, Position(row, column), piece).get))
      else
        createTilesInColumn(column + 1, map + (Position(row, column) -> Tile(White, Position(row, column), piece).get))
    }

    createTilesInColumn(1, Map())
  }
}
