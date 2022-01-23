package backend.domain

import backend.domain.game.{File, Rank}
import chesslogic.pieces.Piece
import derevo.circe.magnolia.decoder
import derevo.derive

object gameLogic {
  @derive(decoder)
  final case class Position(file: File, rank: Rank)
  final case class Tile(piece: Option[Piece])

  final case class UpdatedBoard(tiles: Map[Position, Tile])




}
