package backend.domain

import backend.domain.game.{File, Rank}
import chesslogic.pieces.Piece
import io.circe.Codec

import backend.domain.*

object gameLogic {
  

  final case class Position(file: File, rank: Rank) derives Codec.AsObject {
    def toDomain: chesslogic.board.Position = chesslogic.board.Position(
      rank.toNumber,file.toNumber
    )
  }


  final case class Tile(piece: Option[Piece]) derives Codec.AsObject


  final case class UpdatedBoard(tiles: Map[Position, Tile])




}
