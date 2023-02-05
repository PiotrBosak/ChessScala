// package backend.http.routes.game
//
// import io.odin.Logger
// import gateway.algebras.GameSearchAlg
// import gateway.domain.gamesearch
// import gateway.domain.gamesearch.PollResult.GameFound
// import gateway.http.auth.users.CommonUser
// import cats.{ Applicative, Monad }
// import cats.syntax.all.*
// import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
// import org.http4s.dsl.Http4sDsl
// import org.http4s.server.{ AuthMiddleware, Router }
// import org.http4s.{ AuthedRoutes, HttpRoutes }
//
// final case class GameSearchRoutes[F[_]: Monad: Logger](gameSearchAlg: GameSearchAlg[F]) extends Http4sDsl[F] {
//
//   private[game] val prefixPath = "/gameSearch"
//
//   private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of[CommonUser, F] {
//
//     case POST -> Root / "start" as user =>
//       gameSearchAlg
//         .startSearch(user.value.id)
//         .flatMap(Ok(_))
//     case POST -> Root / "poll" as user =>
//       gameSearchAlg
//         .poll(user.value.id)
//         .flatTap {
//           case GameFound(gameId) =>
//             Logger[F].warn(s"FOUND GAME $gameId")
//           case _ => Applicative[F].unit
//         }
//         .flatMap(Ok(_))
//     case POST -> Root / "stop" as user =>
//       gameSearchAlg
//         .stopSearching(user.value.id)
//         .flatMap(Ok(_))
//   }
//
//   def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(
//     prefixPath -> authMiddleware(httpRoutes)
//   )
//
// }
