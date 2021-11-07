package backend.http.routes.auth

import backend.algebras.AuthAlg
import backend.domain.auth.{CreateUser, UserNameInUse}
import backend.domain.tokenEncoder
import backend.http.auth.users.AdminUser
import cats.{Monad, MonadThrow}
import io.circe.Decoder
import dev.profunktor.auth._
import dev.profunktor.auth.jwt._
import cats.implicits._
import backend.ext.http4s.refined._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.{AuthedRoutes, HttpRoutes}
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl

final case class AuthRoutes[F[_] : MonadThrow : JsonDecoder](
                                                              authAlg: AuthAlg[F]
                                                            ) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of {
    case req@POST -> Root / "users" =>
      req.decodeR[CreateUser] { createUser =>
        authAlg
          .newUser(createUser.username.toDomain, createUser.password.toDomain)
          .flatMap(Created(_))
      }
        .recoverWith {
          case UserNameInUse(u) => Conflict(u.show)
        }

  }
}
