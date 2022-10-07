package gameProcessor.domain

import io.circe.Codec
import dev.profunktor.redis4cats.codecs.Codecs.derive
import lib.*
import lib.domain.*
import lib.domain.game.GameId
import lib.domain.auth.*
import lib.logic.board.{Board, Position}
import lib.logic.game.SimpleGame
import lib.domain.IdNewtype

import java.util.UUID

object game {

  final case class Move(from: Position, to: Position) derives Codec.AsObject
  enum MoveResult derives Codec.AsObject {
    case IllegalMove
    case GameNotFound
    case GameAlreadyFinished
    case MoveSuccessful(newBoard: Board)
  }

  sealed trait DrawProposal
  case object DrawProposalSuccessful extends DrawProposal
  case object DrawProposalFailed     extends DrawProposal

  sealed trait DrawProposalAnswer
  case object DrawAccepted extends DrawProposalAnswer
  case object DrawRefused  extends DrawProposalAnswer

  sealed trait DrawProposalAnswerResult
  case object ProposalAnswerSuccessful extends DrawProposalAnswerResult
  case object ProposalAnswerFailed     extends DrawProposalAnswerResult

  sealed trait ForfeitResult
  case object ForfeitSuccessful extends ForfeitResult
  case object ForfeitFailed     extends ForfeitResult

}
