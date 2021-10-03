package backend.algebras

import backend.domain.auth.{EncryptedPassword, UserId, UserName}
import backend.http.auth.users.User

trait UserAlg[F[_]] {

  def find(
          username: UserName
          ): F[Option[User]]

  def create(
            userName: UserName,
            password: EncryptedPassword
            ): F[UserId]

}
