package backend.config

import scala.concurrent.duration._
import backend.config.AppEnvironment._
import backend.config.types._
import cats.effect.Async
import cats.syntax.all._
import ciris._
import ciris.refined._
import com.comcast.ip4s._
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
object Config {

  def load[F[_]: Async]: F[AppConfig] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default[F](
            RedisURI("redis://localhost"),
          )
        case Prod =>
          default[F](
            RedisURI("redis://10.123.154.176")
          )
      }
      .load[F]

  private def default[F[_]](
                             redisUri: RedisURI,
                           ): ConfigValue[F, AppConfig] =
    (
      env("SC_JWT_SECRET_KEY").as[JwtSecretKeyConfig].secret,
      env("SC_JWT_CLAIM").as[JwtClaimConfig].secret,
      env("SC_ACCESS_TOKEN_SECRET_KEY").as[JwtAccessTokenKeyConfig].secret,
      env("SC_ADMIN_USER_TOKEN").as[AdminUserTokenConfig].secret,
      env("SC_PASSWORD_SALT").as[PasswordSalt].secret,
    ).parMapN { (jwtSecretKey, jwtClaim, tokenKey, adminToken, salt) =>
      AppConfig(
        AdminJwtConfig(jwtSecretKey, jwtClaim, adminToken),
        tokenKey,
        TokenExpiration(30.minutes),
        salt,
        HttpClientConfig(
          timeout = 60.seconds,
          idleTimeInPool = 30.seconds
        ),
        PostgreSQLConfig(
          host = "localhost",
          port = 5432,
          user = "postgres",
          password = "password",
          database = "store",
          max = 10
        ),
        RedisConfig(redisUri),
        HttpServerConfig(
          host = host"0.0.0.0",
          port = port"8080"
        )
      )
    }

}
