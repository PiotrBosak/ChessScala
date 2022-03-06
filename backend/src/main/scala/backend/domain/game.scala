package backend.domain

import backend.domain.auth.UserId
import backend.domain.gameLogic.Position
import chesslogic.board.Board
import chesslogic.game.SimpleGame
import io.circe.Codec

import dev.profunktor.redis4cats.codecs.Codecs.derive

import java.util.UUID

object game {


  enum Rank {
    case One
    case Two
    case Three
    case Four
    case Five
    case Six
    case Seven
    case Eight

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


  enum File {
    case A
    case B
    case C
    case D
    case E
    case F
    case G
    case H

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
  case object DrawProposalFailed extends DrawProposal


  sealed trait DrawProposalAnswer
  case object DrawAccepted extends DrawProposalAnswer
  case object DrawRefused extends DrawProposalAnswer


  sealed trait DrawProposalAnswerResult
  case object ProposalAnswerSuccessful extends DrawProposalAnswerResult
  case object ProposalAnswerFailed extends DrawProposalAnswerResult


  sealed trait ForfeitResult
  case object ForfeitSuccessful extends ForfeitResult
  case object ForfeitFailed extends ForfeitResult

}
