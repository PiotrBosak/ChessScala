package lib.effects

import io.circe.Encoder
import io.circe.syntax.*

trait RedisEncode[A]:
  def redisEncode(a: A): String

object RedisEncode:
  def apply[A: RedisEncode]: RedisEncode[A] = summon

  given forJson[A: Encoder]: RedisEncode[A] with
    def redisEncode(a: A): String =
      val json = a.asJson.noSpaces
      val afterFirst =
        if (json.charAt(0) == '"')
          json.substring(1)
        else json
      if (afterFirst.charAt(afterFirst.length - 1) == '"')
        afterFirst.substring(0, afterFirst.length - 1)
      else afterFirst

object RedisEncodeExt:

  import RedisEncodeExt._

  extension [A: RedisEncode](a: A) def asRedis: String = RedisEncode[A].redisEncode(a)
