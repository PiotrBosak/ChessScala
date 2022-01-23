package backend.algebras

import backend.domain.game.{DrawProposal, DrawProposalAnswer, DrawProposalAnswerResult, ForfeitResult, GameId, Move, MoveResult}

trait GameAlg[F[_]] {


  def makeMove(gameId: GameId, move: Move): F[MoveResult]

  def proposeDraw(gameId: GameId): F[DrawProposal]

  def answerDrawProposal(gameId: GameId, drawProposalAnswer: DrawProposalAnswer): F[DrawProposalAnswerResult]

  def forfeit(gameId: GameId): F[ForfeitResult]

}
