package backend.http.auth

import backend.domain.auth.{Email, EncryptedPassword, UserId, UserName}
import backend.http.jwt.jwt.JwtSymmetricAuth
import cats.{Eq, Show}
import io.circe.Codec


object users {

     case class AdminJwtAuth(value: JwtSymmetricAuth)
     case class UserJwtAuth(value: JwtSymmetricAuth)


  case class User(id: UserId, name: UserName, email: Email)derives Codec.AsObject


  case class UserWithPassword(id: UserId, name: UserName, email: Email, password: EncryptedPassword)derives Codec.AsObject

  case class CommonUser(value: User)derives Codec.AsObject

  case class AdminUser(value: User)derives Codec.AsObject

}
