package chess.ws

import cats.Monad
import lib.domain.*
import lib.effects.*
import cats.syntax.all.*
import lib.domain.SocketId
import lib.domain.SocketId.*
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.websocket.WebSocketBuilder

final class WsRoutes[F[_]: GenUUID: Logger: Monad](
    ws: WebSocketBuilder[F],
    mkHandler: SocketId => F[Handler[F]]
) extends Http4sDsl[F]:

  // format: off
  val routes: HttpRoutes[F] = HttpRoutes.of {
    case GET -> Root / "v1" / "ws" =>
      GenUUID[F].make
        .map(uuid => SocketId.apply(uuid))
        .flatMap(mkHandler)
        .flatMap(h => ws.build(h.send, h.receive))
  }
