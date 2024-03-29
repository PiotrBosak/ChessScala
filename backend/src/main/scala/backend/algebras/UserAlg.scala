package backend.algebras

import backend.domain.ID
import backend.domain.auth._
import backend.effects.GenUUID
import backend.http.auth.users._
import backend.sql.codecs._

import cats.effect._
import cats.syntax.all._
import skunk._
import skunk.implicits._
trait UserAlg[F[_]] {

  def find(username: UserName): F[Option[UserWithPassword]]

  def create(username: UserName, email: UserEmail, password: EncryptedPassword): F[UserId]
}

object UserAlg {
  def make[F[_] : GenUUID : MonadCancelThrow](
                                               postgres: Resource[F, Session[F]]
                                             ): UserAlg[F] =
    new UserAlg[F] {

      import UserSQL._

      def find(username: UserName): F[Option[UserWithPassword]] =
        postgres.use { session =>
          session.prepare(selectUser).use { q =>
            q.option(username).map {
              case Some(u ~ p) => UserWithPassword(u.id, u.name, u.email, p).some
              case _ => none[UserWithPassword]
            }
          }
        }

      def create(username: UserName, email: UserEmail, password: EncryptedPassword): F[UserId] =
        postgres.use { session =>
          session.prepare(insertUser).use { cmd =>
            ID.make[F, UserId].flatMap { id =>
              cmd
                .execute(User(id, username, email) ~ password)
                .as(id)
                .recoverWith {
                  case SqlState.UniqueViolation(_) =>
                    UserNameInUse(username).raiseError[F, UserId]
                }
            }
          }
        }
    }

}

private object UserSQL {

  val codec: Codec[User ~ EncryptedPassword] =
    (userId ~ userName ~ userEmail ~ encPassword).imap {
      case i ~ n ~ e ~ p =>
        User(i, n, e) ~ p
    } {
      case u ~ p =>
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
