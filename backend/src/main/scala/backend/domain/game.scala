package backend.domain

import backend.domain.gameLogic.Position
import chesslogic.board.Board
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import dev.profunktor.redis4cats.codecs.Codecs.derive
import io.estatico.newtype.macros.newtype

import java.util.UUID

object game {


  @derive(decoder)
  sealed trait Rank
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

  @derive(decoder)
  sealed trait File
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

  @derive(decoder)
  final case class Move(from: Position, to: Position)

  @derive(encoder)
  sealed trait MoveResult
  final case object IllegalMove extends MoveResult
  final case object GameNotFound extends MoveResult
  final case object GameAlreadyFinished extends MoveResult
  final case class MoveSuccessful(newBoard: Board)

  @derive(encoder)
  @newtype
  final case class GameId(value: UUID)

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
