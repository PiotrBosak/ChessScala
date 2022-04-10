package backend.domain

import backend.domain.game.GameId


import io.circe.syntax.*
import io.circe.{Encoder, Json,Codec}

import java.time.Duration

object gamesearch {



  final case class InitialTryDuration(duration: Duration)


  enum StartSearchResult derives Codec.AsObject:
    case SearchStartSuccessful
    case SearchFailure
    case GameAlreadyStarted


  enum PokeResult:
    case GameNotFoundYet
    case GameFound(gameId: GameId)
    case PokeFailure

  object PokeResult {
    implicit  val pokeResultEncoder: Encoder[PokeResult] = Encoder.instance {
      case GameNotFoundYet => Json.fromFields(List(("tag", Json.fromString("gameNotFoundYet"))))
      case GameFound(gameId) => Json.fromFields(
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
