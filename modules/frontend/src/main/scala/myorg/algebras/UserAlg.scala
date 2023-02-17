package myorg.algebras

import lib.user.*
import lib.user.Profile
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.IO
import cats.implicits.*
import org.http4s.{Entity, Request, Uri}
import cats.effect.Resource
import myorg.pages.Main

trait UserAlg[F[_]] {

  def login(loginData: LoginData): F[Option[Profile]]

  def register(registrationData: RegistrationData): F[Option[Profile]]

  def logout(jwt: JwtTokenParam): F[Unit]

}
object UserAlg {
  def apply[F[_]](using userAlg: UserAlg[F]): UserAlg[F] = summon
  given UserAlg[IO] = new UserAlg[IO] {

    def login(loginData: LoginData): IO[Option[Profile]] = ???

    def register(registrationData: RegistrationData): IO[Option[Profile]] = {
      IO.println("HELLLLLLLLLLLLLLLLLLLLLOL") >>
      EmberClientBuilder
        .default[IO]
        .build
        .use { client =>
          client
            .run {
              Request[IO](
                uri = Uri.fromString("localhost:8080/users") match {
                  case Left(_)  => throw new RuntimeException()
                  case Right(a) => a
                }
              )
            }
            .use { resp =>
              println("Got response!!!!!!!!!!!!")
              IO(None)
            }
        }

    }
    def logout(jwt: JwtTokenParam): IO[Unit] = IO.unit

  }
}
