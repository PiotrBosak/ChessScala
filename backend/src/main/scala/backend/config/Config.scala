package backend.config

import ciris.*
import backend.domain.{*, given}
import com.comcast.ip4s.*

import scala.concurrent.duration.*
import backend.config.AppEnvironment.*
import backend.config.types.*
import cats.effect.Async
import cats.syntax.all.*
import ciris.*
import ciris.refined.*
import com.comcast.ip4s.*
import eu.timepit.refined.auto.*
import eu.timepit.refined.cats.*
import eu.timepit.refined.types.string.NonEmptyString
import org.tpolecat.typename.TypeName

object Config {


  given cirisConfigDecoder: ConfigDecoder[String, AppEnvironment] =
    ConfigDecoder[String].mapOption("AppEnvironment") { s =>
      s.toLowerCase match
        case "test" => Some(Test)
        case "prod" => Some(Prod)
        case _ => None
    }

  def load[F[_] : Async]: F[AppConfig] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default[F](
            RedisURI("redis://localhost:6379"),
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
        TokenExpiration(30.minutes.toMillis.toInt),
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
          host = host"localhost",
          port = port"8080"
        )
      )
    }

}
