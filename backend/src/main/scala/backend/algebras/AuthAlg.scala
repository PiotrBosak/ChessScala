package backend.algebras

import backend.auth.{ Crypto, Tokens }
import backend.config.types.TokenExpiration
import backend.domain.jwt.*
import backend.domain.*
import backend.domain.auth.*
import backend.http.auth.users.*
import eu.timepit.refined.auto.autoUnwrap
import cats.*
import cats.syntax.all.*
import backend.domain.jwt.JwtToken
import dev.profunktor.redis4cats.RedisCommands
import backend.domain.RedisEncodeExt.asRedis
import io.circe.parser.decode
import io.circe.syntax.*
import pdi.jwt.JwtClaim

import scala.concurrent.duration.{ FiniteDuration, MILLISECONDS }

trait AuthAlg[F[_]] {

  def newUser(username: UserName, email: Email, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, userName: UserName): F[Unit]
}

trait UsersAuthAlg[F[_], A] {
  def findUser(token: JwtToken)(claim: JwtClaim): F[Option[A]]
}

object UsersAuth {
  def admin[F[_]: Applicative](
      adminToken: JwtToken,
      adminUser: AdminUser
  ): UsersAuthAlg[F, AdminUser] =
    new UsersAuthAlg[F, AdminUser] {
      def findUser(token: JwtToken)(claim: JwtClaim): F[Option[AdminUser]] =
        (token == adminToken)
          .guard[Option]
          .as(adminUser)
          .pure[F]
    }

  def common[F[_]: Functor](
      redis: RedisCommands[F, String, String]
  ): UsersAuthAlg[F, CommonUser] =
    new UsersAuthAlg[F, CommonUser] {
      def findUser(token: JwtToken)(claim: JwtClaim): F[Option[CommonUser]] =
        redis
          .get(token.value)
          .map {
            _.flatMap { u =>
              decode[User](u).toOption.map(CommonUser.apply)
            }
          }
    }

}

object AuthAlg {
  def make[F[_]: MonadThrow](
      tokenExpiration: TokenExpiration,
      tokens: Tokens[F],
      users: UserAlg[F],
      redis: RedisCommands[F, String, String],
      crypto: Crypto
  ): AuthAlg[F] =
    new AuthAlg[F] {

      private val TokenExpiration = FiniteDuration(tokenExpiration.value, MILLISECONDS)

      def newUser(username: UserName, email: Email, password: Password): F[JwtToken] =
        users.find(username).flatMap {
          case Some(_) => UserNameInUse(username).raiseError[F, JwtToken]
          case None =>
            for {
              i <- users.create(username, email, crypto.encrypt(password))
              t <- tokens.create
              u = User(i, username, email).asRedis
              _ <- redis.setEx(t.value.asRedis, u, TokenExpiration)
              _ <- redis.setEx(username.asRedis, t.value, TokenExpiration)
            } yield t
        }

      def login(username: UserName, password: Password): F[JwtToken] =
        users.find(username).flatMap {
          case None => UserNotFound(username).raiseError[F, JwtToken]
          case Some(user) if user.password =!= crypto.encrypt(password) =>
            InvalidPassword(user.name).raiseError[F, JwtToken]
          case Some(user) =>
            redis.get(username.show).flatMap {
              case Some(t) => JwtToken(t).pure[F]
              case None =>
                tokens.create.flatTap { t =>
                  redis.setEx(t.asRedis, user.asRedis, TokenExpiration) *>
                    redis.setEx(username.asRedis, t.asRedis, TokenExpiration)
                }
            }
        }

      def logout(token: JwtToken, username: UserName): F[Unit] =
        redis.del(token.asRedis) *> redis.del(username.asRedis).void

    }
}
