package chess.ws

import cats.effect.*
import cats.syntax.all.*
import dev.profunktor.pulsar.{ Consumer as PulsarConsumer, Pulsar, Subscription }
import fs2.Stream
import org.apache.pulsar.client.api.SubscriptionInitialPosition

object Main extends IOApp.Simple:
  def run: IO[Unit] = IO.unit
