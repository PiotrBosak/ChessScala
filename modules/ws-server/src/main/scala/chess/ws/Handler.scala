package chess.ws

import cats.Monad
import lib.*
import lib.domain.*
import cats.effect.kernel.{Concurrent, Deferred, Ref}
import cats.syntax.all.*
import fs2.{Pipe, Stream}
import io.circe.parser.decode as jsonDecode
import io.circe.syntax.*
import io.odin.Logger
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.{Close, Text}

trait Handler[F[_]]:
  def send: Stream[F, WebSocketFrame]
  def receive: Pipe[F, WebSocketFrame, Unit]

object Handler:
  def make[F[_]: Concurrent: Logger](
      sid: SocketId,
      conns: WsConnections[F],
      alerts: Stream[F, Alert]
  ): F[Handler[F]] =
    Monad[F].pure(new Handler[F] {
      override def send: Stream[F, WebSocketFrame] = Stream.empty

      override def receive: Pipe[F, WebSocketFrame, Unit] = _ => Stream.empty
    })
