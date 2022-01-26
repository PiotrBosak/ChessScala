package backend.domain

import backend.domain.game.{File, Rank}
import chesslogic.pieces.Piece
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import backend.domain._

object gameLogic {
  
  @derive(decoder, encoder)
  final case class Position(file: File, rank: Rank) {
    def toDomain: chesslogic.board.Position = chesslogic.board.Position(
      rank.toNumber,file.toNumber
    )
  }

  @derive(encoder,decoder)
  final case class Tile(piece: Option[Piece])

  @derive(encoder,decoder)
  final case class UpdatedBoard(tiles: Map[Position, Tile])




}
