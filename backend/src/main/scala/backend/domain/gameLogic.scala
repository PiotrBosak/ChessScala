package backend.domain

import chesslogic.pieces.Piece
import io.circe.Codec
import chesslogic.board.Position
import backend.domain.*

object gameLogic {

  final case class Tile(piece: Option[Piece]) derives Codec.AsObject

  final case class UpdatedBoard(tiles: Map[Position, Tile])

}
