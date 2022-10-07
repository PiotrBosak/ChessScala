package gateway.modules

import gateway.auth.*
import lib.domain.auth.*
import lib.config.types.*
import gateway.domain.jwt.*
import gateway.domain.auth.*
import gateway.http.auth.users.*
import gateway.algebras.*
import cats.{ Applicative, ApplicativeThrow }
import cats.effect.*
import cats.syntax.all.*
import dev.profunktor.redis4cats.RedisCommands
import eu.timepit.refined.auto.*
import io.circe.parser.decode as jsonDecode
import pdi.jwt.*
import gateway.http.jwt.jwt._
import skunk.Session

import java.util.UUID
object Security {
  def make[F[_]: Sync](
      cfg: AppConfig,
      postgres: Resource[F, Session[F]],
      redis: RedisCommands[F, String, String]
  ): F[Security[F]] = {

    val adminJwtAuth: AdminJwtAuth =
      AdminJwtAuth(
        JwtAuth
          .hmac(
            cfg.adminJwtConfig.secretKey.value.value,
            JwtAlgorithm.HS256
          )
      )

    val userJwtAuth: UserJwtAuth =
      UserJwtAuth(
        JwtAuth
          .hmac(
            cfg.tokenConfig.value.value,
            JwtAlgorithm.HS256
          )
      )

    //todo to tez jest raczej zjebane
    val adminToken = JwtToken(cfg.adminJwtConfig.adminToken.value.value)

    for {
      adminClaim <- jwtDecode[F](adminToken, adminJwtAuth.value)
      content    <- ApplicativeThrow[F].fromEither(jsonDecode[ClaimContent](adminClaim.content))
      //todo fix
      _ <- Applicative[F].unit
      adminUser = AdminUser(User(UserId(UUID.randomUUID()), UserName("admin"), Email("admin@myChessMail.com")))
      tokens <- JwtExpire.make[F].map(Tokens.make[F](_, cfg.tokenConfig.value, cfg.tokenExpiration))
      crypto <- Crypto.make[F](cfg.passwordSalt.value)
      users     = UserAlg.make[F](postgres)
      auth      = AuthAlg.make[F](cfg.tokenExpiration, tokens, users, redis, crypto)
      adminAuth = UsersAuth.admin[F](adminToken, adminUser)
      usersAuth = UsersAuth.common[F](redis)
    } yield new Security[F](auth, adminAuth, usersAuth, adminJwtAuth, userJwtAuth) {}

  }
}

sealed abstract class Security[F[_]] private (
    val auth: AuthAlg[F],
    val adminAuth: UsersAuthAlg[F, AdminUser],
    val usersAuth: UsersAuthAlg[F, CommonUser],
    val adminJwtAuth: AdminJwtAuth,
    val userJwtAuth: UserJwtAuth
)
