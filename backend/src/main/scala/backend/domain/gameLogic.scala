package backend.domain

import io.circe.Codec
import backend.domain.*
import lib.logic.board.Position
import lib.logic.pieces.Piece

object gameLogic {

  final case class Tile(piece: Option[Piece]) derives Codec.AsObject

  final case class UpdatedBoard(tiles: Map[Position, Tile])

}
