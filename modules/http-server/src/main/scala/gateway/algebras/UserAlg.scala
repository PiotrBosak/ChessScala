package gateway.algebras

import lib.domain.ID
import lib.domain.auth.*
import lib.domain.*
import gateway.domain.auth.*
import lib.effects.GenUUID
import gateway.http.auth.users.*
import gateway.sql.codecs.*
import cats.effect.*
import cats.syntax.all.*
import lib.domain.auth.*
import skunk.*
import skunk.implicits.*

trait UserAlg[F[_]] {

  def find(username: UserName): F[Option[UserWithPassword]]

  def create(username: UserName, email: Email, password: EncryptedPassword): F[UserId]
}

object UserAlg {
  def make[F[_]: GenUUID: MonadCancelThrow](
      postgres: Resource[F, Session[F]]
  ): UserAlg[F] =
    new UserAlg[F] {

      import UserSQL.*

      def find(username: UserName): F[Option[UserWithPassword]] =
        postgres.use { session =>
          session.prepare(selectUser).use { q =>
            q.option(username).map {
              case Some(u ~ p) => UserWithPassword(u.id, u.name, u.email, p).some
              case _           => none[UserWithPassword]
            }
          }
        }

      def create(username: UserName, email: Email, password: EncryptedPassword): F[UserId] =
        postgres.use { session =>
          session.prepare(insertUser).use { cmd =>
            ID.make[F, UserId].flatMap { id =>
              cmd
                .execute(User(id, username, email) ~ password)
                .as(id)
                .recoverWith { case SqlState.UniqueViolation(_) =>
                  UserNameInUse(username).raiseError[F, UserId]
                }
            }
          }
        }
    }

}

private object UserSQL {

  val codec: Codec[User ~ EncryptedPassword] =
    (userId ~ userName ~ email ~ encPassword).imap { case i ~ n ~ e ~ p =>
      User(i, n, e) ~ p
    } { case u ~ p =>
      u.id ~ u.name ~ u.email ~ p
    }

  val selectUser: Query[UserName, User ~ EncryptedPassword] =
    sql"""
        SELECT * FROM users
        WHERE name = $userName
       """.query(codec)

  val insertUser: Command[User ~ EncryptedPassword] =
    sql"""
        INSERT INTO users
        VALUES ($codec)
        """.command

}
