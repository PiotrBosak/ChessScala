package backend.algebras

import backend.domain.auth.{Password, UserName}
import backend.http.auth.users.User
import dev.profunktor.auth.jwt.JwtToken

trait AuthAlg[F[_]] {

  def findUser(token: JwtToken): F[Option[User]]
  def newUser(username: UserName, password: Password): F[JwtToken]

  def login(username:UserName, password: Password): F[JwtToken]

  def logout(token: JwtToken, userName: UserName) : F[Unit]

}
