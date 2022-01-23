package backend.domain

import backend.domain.game.GameId
import derevo.circe.magnolia.encoder
import derevo.derive
import io.estatico.newtype.macros.newtype

import java.time.Duration

object gamesearch {

  @derive(encoder)
  @newtype
  final case class InitialTryDuration(duration: Duration)

  @derive(encoder)
  @newtype
  final case class NextTryTimeout(duration: Duration)

  @derive(encoder)
  sealed trait StartSearchResult

  @derive(encoder)
  final case class SearchSuccessful(initialTryDuration: InitialTryDuration) extends StartSearchResult
  final case object SearchFailure extends StartSearchResult

  @derive(encoder)
  sealed trait PokeResult
  final case class GameNotFoundYet(nextTryTimeout: NextTryTimeout) extends PokeResult
  final case class GameFound(gameId: GameId) extends PokeResult
  final case object PokeFailure extends PokeResult

  @derive(encoder)
  sealed trait StopSearchResult
  final case object SearchStopSuccessful extends StopSearchResult
  final case object GameAlreadyFound extends StopSearchResult


}
