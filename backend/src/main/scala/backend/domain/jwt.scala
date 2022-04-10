package backend.domain

import org.http4s.Credentials.Token
import org.http4s.{ AuthScheme, Request }
import org.http4s.headers.Authorization

object jwt {

  type JwtToken = JwtToken.Type
  object JwtToken extends Newtype[String]

  object AuthHeaders {
    def getBearerToken[F[_]](request: Request[F]): Option[JwtToken] =
      request.headers.get[Authorization].collect { case Authorization(Token(AuthScheme.Bearer, token)) =>
        JwtToken(token)
      }
  }
}
