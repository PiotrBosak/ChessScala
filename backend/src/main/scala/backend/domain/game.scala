package backend.domain

import backend.domain.auth.UserId
import chesslogic.board.Board
import chesslogic.board.position.Position
import chesslogic.game.SimpleGame
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype

import java.util.UUID

object game {


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
