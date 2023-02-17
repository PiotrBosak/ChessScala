package lib
import java.util.UUID
import cats.implicits.*
object user {
  final case class UserIdParam(value: UUID)
  final case class UserNameParam private (value: String)
  object UserNameParam {
    def make(s: String): Option[UserNameParam] =
      if (s.length > 3)
        UserNameParam(s).some
      else None

  }
  final case class PasswordParam private (value: String)
  object PasswordParam {
    def make(s: String): Option[PasswordParam] =
      if (s.length > 3 && s.exists(_.isDigit))
        PasswordParam(s).some
      else None
  }
  final case class EmailParam private (value: String)
  object EmailParam {
    def make(s: String): Option[EmailParam] =
      if (s.length > 3 & s.contains("@"))
        EmailParam(s).some
      else None
  }
  final case class JwtTokenParam(value: String)
  final case class Profile(userName: UserNameParam, token: JwtTokenParam)

  final case class RegistrationData(username: UserNameParam,
                                    email: EmailParam,
                                    password: PasswordParam)
  final case class LoginData(username: UserNameParam, password: PasswordParam)
}
