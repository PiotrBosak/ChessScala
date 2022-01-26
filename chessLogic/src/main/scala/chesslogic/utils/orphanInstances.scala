package chesslogic.utils

import chesslogic.board.{Position, Tile}
import io.circe.{Decoder, Encoder, Json, KeyDecoder, KeyEncoder}

object orphanInstances {
  implicit val keyPositionEncoder: KeyEncoder[Position] = KeyEncoder.instance(Encoder[Position].apply(_).noSpaces)
  implicit val keyPositionDecoder: KeyDecoder[Position] = KeyDecoder.instance(j => Decoder[Position].decodeJson(Json.fromString(j)).toOption)
  implicit val encodeMap = Encoder.encodeMap[Position,Tile]
  implicit val decodeMap = Decoder.decodeMap[Position,Tile]
  implicit val encodeBackendMap = Encoder.encodeMap[Position,Tile]
}
