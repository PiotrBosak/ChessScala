package gateway.domain

import lib.domain.game.GameId

import io.circe.syntax.*
import io.circe.{ Codec, Encoder, Json }

import java.time.Duration

object gamesearch {

  final case class InitialTryDuration(duration: Duration)

  enum StartSearchResult derives Codec.AsObject:
    case SearchStartSuccessful
    case SearchFailure
    case GameAlreadyStarted

  enum PollResult:
    case GameNotFoundYet
    case GameFound(gameId: GameId)
    case PokeFailure

  object PollResult {
    implicit val pokeResultEncoder: Encoder[PollResult] = Encoder.instance {
      case GameNotFoundYet => Json.fromFields(List(("tag", Json.fromString("gameNotFoundYet"))))
      case GameFound(gameId) =>
        Json.fromFields(
          List(
            ("tag", Json.fromString("gameFound")),
            ("value", gameId.asJson)
          )
        )
      case PokeFailure => Json.fromFields(List(("tag", Json.fromString("pokeFailure"))))
    }
  }

  enum StopSearchResult derives Codec.AsObject:
    case SearchStopSuccessful
    case GameAlreadyFound

}
