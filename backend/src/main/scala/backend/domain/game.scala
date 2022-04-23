package backend.domain

import backend.domain.auth.UserId
import chesslogic.board.{Board, Position}
import chesslogic.game.SimpleGame
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import dev.profunktor.redis4cats.codecs.Codecs.derive
import io.estatico.newtype.macros.newtype

import java.util.UUID

object game {


  @derive(decoder, encoder)
  sealed trait Rank {
    def toNumber = this match {
      case Rank.One => 1
      case Rank.Two => 2
      case Rank.Three => 3
      case Rank.Four => 4
      case Rank.Five => 5
      case Rank.Six => 6
      case Rank.Seven => 7
      case Rank.Eight => 8
    }
  }
  object Rank {
    final case object One extends Rank
    final case object Two  extends Rank
    final case object Three extends Rank
    final case object Four extends Rank
    final case object Five extends Rank
    final case object Six extends Rank
    final case object Seven extends Rank
    final case object Eight extends Rank
  }

  @derive(decoder, encoder)
  sealed trait File {
    def toNumber: Int = this match {
      case File.A => 1
      case File.B => 2
      case File.C => 3
      case File.D => 4
      case File.E => 5
      case File.F => 6
      case File.G => 7
      case File.H => 8
    }
  }
  object File {
    final case object A extends File
    final case object B extends File
    final case object C extends File
    final case object D extends File
    final case object E extends File
    final case object F extends File
    final case object G extends File
    final case object H extends File
  }

  @derive(encoder, decoder)
  final case class Move(from: Position, to: Position)

  @derive(encoder)
  sealed trait MoveResult
  final case object IllegalMove extends MoveResult
  final case object GameNotFound extends MoveResult
  final case object GameAlreadyFinished extends MoveResult
  final case class MoveSuccessful(newBoard: Board) extends MoveResult

  @derive(encoder, decoder)
  @newtype
  final case class GameId(value: UUID)

  @derive(encoder,decoder)
  final case class PvPGame(
                          whitePlayer: UserId,
                          blackPlayer: UserId,
                          gameId: GameId,
                          simpleGame: SimpleGame
                          )

  @derive(encoder)
  sealed trait DrawProposal
  final case object DrawProposalSuccessful extends DrawProposal
  final case object DrawProposalFailed extends DrawProposal

  @derive(decoder)
  sealed trait DrawProposalAnswer
  final case object DrawAccepted extends DrawProposalAnswer
  final case object DrawRefused extends DrawProposalAnswer

  @derive(encoder)
  sealed trait DrawProposalAnswerResult
  final case object ProposalAnswerSuccessful extends DrawProposalAnswerResult
  final case object ProposalAnswerFailed extends DrawProposalAnswerResult

  @derive(encoder)
  sealed trait ForfeitResult
  final case object ForfeitSuccessful extends ForfeitResult
  final case object ForfeitFailed extends ForfeitResult

}
