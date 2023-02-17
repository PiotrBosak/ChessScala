package lib.logic.board.util
import lib.logic.board.{Position, Tile}

import io.circe.{Decoder, Encoder, Json, KeyDecoder, KeyEncoder}
package codec {

  given keyPositionEncoder: KeyEncoder[Position] = KeyEncoder.instance(Encoder[Position].apply(_).noSpaces)
  given keyPositionDecoder: KeyDecoder[Position] = KeyDecoder.instance(j => Decoder[Position].decodeJson(Json.fromString(j)).toOption)
  given encodeMap: Encoder[Map[Position, Tile]] = Encoder.encodeMap[Position, Tile]
  given decodeMap: Decoder[Map[Position, Tile]] = Decoder.decodeMap[Position, Tile]
}
