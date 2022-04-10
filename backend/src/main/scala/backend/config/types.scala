package backend.config

import backend.domain.Newtype
import ciris.*
import ciris.refined.*
import com.comcast.ip4s.{ Host, Port }
import eu.timepit.refined.cats.*
import eu.timepit.refined.types.net.UserPortNumber

import scala.concurrent.duration.*

object types {

  type AdminUserTokenConfig = AdminUserTokenConfig.Type
  object AdminUserTokenConfig extends Newtype[String]

  type JwtSecretKeyConfig = JwtSecretKeyConfig.Type
  object JwtSecretKeyConfig extends Newtype[String]

  type JwtAccessTokenKeyConfig = JwtAccessTokenKeyConfig.Type
  object JwtAccessTokenKeyConfig extends Newtype[String]
  type JwtClaimConfig = JwtClaimConfig.Type
  object JwtClaimConfig extends Newtype[String]
  type PasswordSalt = PasswordSalt.Type
  object PasswordSalt extends Newtype[String]

  type TokenExpiration = TokenExpiration.Type
  object TokenExpiration extends Newtype[Int]
  type ShoppingCartExpiration = ShoppingCartExpiration.Type
  object ShoppingCartExpiration extends Newtype[Int]

  case class CheckoutConfig(
      retriesLimit: Int,
      retriesBackoff: FiniteDuration
  )

  case class AppConfig(
      adminJwtConfig: AdminJwtConfig,
      tokenConfig: Secret[JwtAccessTokenKeyConfig],
      tokenExpiration: TokenExpiration,
      passwordSalt: Secret[PasswordSalt],
      httpClientConfig: HttpClientConfig,
      postgreSQL: PostgreSQLConfig,
      redis: RedisConfig,
      httpServerConfig: HttpServerConfig
  )

  case class AdminJwtConfig(
      secretKey: Secret[JwtSecretKeyConfig],
      claimStr: Secret[JwtClaimConfig],
      adminToken: Secret[AdminUserTokenConfig]
  )

  case class PostgreSQLConfig(
      host: String,
      port: Int,
      user: String,
      password: String,
      database: String,
      max: Int
  )

  type RedisURI = RedisURI.Type
  object RedisURI extends Newtype[String]
  type RedisConfig = RedisConfig.Type
  object RedisConfig extends Newtype[RedisURI]
  type PaymentURI = PaymentURI.Type
  object PaymentURI extends Newtype[String]
  type PaymentConfig = PaymentConfig.Type
  object PaymentConfig extends Newtype[PaymentURI]

  case class HttpServerConfig(
      host: Host,
      port: Port
  )

  case class HttpClientConfig(
      timeout: FiniteDuration,
      idleTimeInPool: FiniteDuration
  )

}
