package backend.modules

import backend.auth._
import backend.config.types._
import backend.domain.auth._
import backend.http.auth.users._
import backend.algebras._

import cats.ApplicativeThrow
import cats.effect._
import cats.syntax.all._
import dev.profunktor.auth.jwt._
import dev.profunktor.redis4cats.RedisCommands
import eu.timepit.refined.auto._
import io.circe.parser.{ decode => jsonDecode }
import pdi.jwt._
import skunk.Session
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
            cfg.adminJwtConfig.secretKey.value.secret,
            JwtAlgorithm.HS256
          )
      )

    val userJwtAuth: UserJwtAuth =
      UserJwtAuth(
        JwtAuth
          .hmac(
            cfg.tokenConfig.value.secret,
            JwtAlgorithm.HS256
          )
      )

    val adminToken = JwtToken(cfg.adminJwtConfig.adminToken.value.secret)

    for {
      adminClaim <- jwtDecode[F](adminToken, adminJwtAuth.value)
      content    <- ApplicativeThrow[F].fromEither(jsonDecode[ClaimContent](adminClaim.content))
      adminUser = AdminUser(User(UserId(content.uuid), UserName("admin"), UserEmail("admin@chess.com")))
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