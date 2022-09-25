package backend.domain

import eu.timepit.refined.auto.*
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.*

import java.util.UUID
import javax.crypto.Cipher
import scala.util.control.NoStackTrace
import commondomain.*

object auth {

  type UserId = UserId.Type
  object UserId extends IdNewtype

  type UserName = UserName.Type
  object UserName extends Newtype[String]

  type Password = Password.Type
  object Password extends Newtype[String]

  type Email = Email.Type
  object Email extends Newtype[String]

  type EncryptedPassword = EncryptedPassword.Type
  object EncryptedPassword extends Newtype[String]

  case class EncryptCipher(value: Cipher)
//  type EncryptCipher = EncryptCipher.Type
//  object EncryptCipher extends Newtype[Cipher]

  case class DecryptCipher(value: Cipher)
//  type DecryptCipher = DecryptCipher.Type
//  object DecryptCipher extends Newtype[Cipher]

  // --------- user registration -----------

  type UserNameParam = UserNameParam.Type
  object UserNameParam extends Newtype[String]
  object UserNameParamExtensions {
    extension (username: UserNameParam) {
      def toDomain: UserName = UserName(username.value.toLowerCase)
    }
  }

  type EmailParam = EmailParam.Type
  object EmailParam extends Newtype[String]
  object EmailParamExtensions {
    extension (email: EmailParam) {
      def toDomain: Email = Email(email.value)
    }
  }

  type PasswordParam = PasswordParam.Type
  object PasswordParam extends Newtype[String]
  object PasswordParamExtensions {
    extension (password: PasswordParam) {
      def toDomain: Password = Password(password.value)
    }
  }

  case class CreateUser(
      username: UserNameParam,
      email: EmailParam,
      password: PasswordParam
  ) derives Codec.AsObject

  case class UserNotFound(username: UserName)    extends NoStackTrace derives Codec.AsObject
  case class UserNameInUse(username: UserName)   extends NoStackTrace derives Codec.AsObject
  case class InvalidPassword(username: UserName) extends NoStackTrace derives Codec.AsObject
  case object UnsupportedOperation               extends NoStackTrace derives Codec.AsObject

  case object TokenNotFound extends NoStackTrace

  // --------- user login -----------

  case class LoginUser(
      username: UserNameParam,
      password: PasswordParam
  ) derives Codec.AsObject

  // --------- admin auth -----------

  case class ClaimContent(uuid: UUID)

  object ClaimContent {
    implicit val jsonDecoder: Decoder[ClaimContent] =
      Decoder.forProduct1("uuid")(ClaimContent.apply)
  }

}
