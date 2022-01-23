package backend.algebras

import backend.domain.auth.{EncryptedPassword, UserId, UserName}
import backend.http.auth.users.{User, UserWithPassword}

trait UserAlg[F[_]] {

  def find(username: UserName): F[Option[UserWithPassword]]
  def create(username: UserName, password: EncryptedPassword): F[UserId]
}
