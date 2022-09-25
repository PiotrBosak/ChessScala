package backend.domain

import backend.domain.auth.UserId
import chesslogic.board.{ Board, Position }
import chesslogic.game.SimpleGame
import io.circe.Codec
import dev.profunktor.redis4cats.codecs.Codecs.derive
import commondomain.*

import java.util.UUID

object game {

  final case class Move(from: Position, to: Position) derives Codec.AsObject
  enum MoveResult derives Codec.AsObject {
    case IllegalMove
    case GameNotFound
    case GameAlreadyFinished
    case MoveSuccessful(newBoard: Board)
  }

  type GameId = GameId.Type
  object GameId extends IdNewtype

  final case class PvPGame(
      whitePlayer: UserId,
      blackPlayer: UserId,
      gameId: GameId,
      simpleGame: SimpleGame
  ) derives Codec.AsObject

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
