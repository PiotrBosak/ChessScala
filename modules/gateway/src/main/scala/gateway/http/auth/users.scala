package gateway.http.auth

import gateway.domain.auth.{ Email, EncryptedPassword }
import lib.domain.auth.{  UserId, UserName }
import gateway.http.jwt.jwt.JwtSymmetricAuth
import lib.domain.auth.*
import cats.{ Eq, Show }
import io.circe.Codec

object users {

  case class AdminJwtAuth(value: JwtSymmetricAuth)
  case class UserJwtAuth(value: JwtSymmetricAuth)

  case class User(id: UserId, name: UserName, email: Email) derives Codec.AsObject

  case class UserWithPassword(id: UserId, name: UserName, email: Email, password: EncryptedPassword)
      derives Codec.AsObject

  case class CommonUser(value: User) derives Codec.AsObject

  case class AdminUser(value: User) derives Codec.AsObject

}
