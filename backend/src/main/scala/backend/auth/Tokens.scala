package backend.auth

import backend.config.types.*
import backend.effects.*
import backend.domain.jwt.*
import cats.Monad
import cats.syntax.all.*
import eu.timepit.refined.auto.*
import io.circe.syntax.*
import pdi.jwt.*

trait Tokens[F[_]] {
  def create: F[JwtToken]
}

object Tokens {
  def make[F[_]: GenUUID: Monad](
      jwtExpire: JwtExpire[F],
      config: JwtAccessTokenKeyConfig,
      exp: TokenExpiration
  ): Tokens[F] =
    new Tokens[F] {
      def create: F[JwtToken] =
        for {
          uuid  <- GenUUID[F].make
          claim <- jwtExpire.expiresIn(JwtClaim(uuid.asJson.noSpaces), exp)
          token = JwtToken("Fix that")
//          secretKey = JwtSecretKey(config.secret.value)
//          token <- jwtEncode[F](claim, secretKey, JwtAlgorithm.HS256)
        } yield token
    }
}
