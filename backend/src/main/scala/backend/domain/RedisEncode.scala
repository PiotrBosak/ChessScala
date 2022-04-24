package backend.domain

import io.circe.syntax._
import io.circe.Encoder

trait RedisEncode[A] {
  def redisEncode(a: A): String

}

object RedisEncode {
  def apply[A: RedisEncode]: RedisEncode[A] = implicitly[RedisEncode[A]]
  implicit def forJson[A : Encoder] : RedisEncode[A] = (a: A) => {
    val json = a.asJson.noSpaces
    val afterFirst =
      if (json.charAt(0) == '"')
        json.substring(1)
      else json
    if (afterFirst.charAt(afterFirst.length - 1) == '"')
      afterFirst.substring(0, afterFirst.length - 1)
    else afterFirst
  }
  implicit class RedisEncodeExt[A: RedisEncode](a: A) {
    def asRedis: String = RedisEncode[A].redisEncode(a)
  }
}

