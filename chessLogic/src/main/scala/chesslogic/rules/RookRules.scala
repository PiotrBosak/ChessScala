package chesslogic.rules
import chesslogic.Color
import chesslogic.board.{Board, Position}
import chesslogic.pieces.Rook
import chesslogic.rules.RulesForMultiTileMoves.{getViablePositionForAttacks, getViablePositionsForMoves}

object RookRules extends MovingRules[Rook] {
  override def getPossibleAttacks(position: Position, board: Board): List[Position] = {
    (for {
      tile <- board.getTile(position)
      piece <- tile.currentPiece
    } yield List(
      getTopAttack(board, position, piece.color),
      getLeftAttack(board, position, piece.color),
      getRightAttack(board, position, piece.color),
      getDownAttack(board, position, piece.color)).collect { case Some(position) => position}
      ).getOrElse(Nil)
  }

  override def getPossibleMoves(position: Position, board: Board): List[Position] = {
    getDownMoves(position, board) ++ getTopMoves(position, board) ++
      getLeftMoves(position, board) ++ getRightMoves(position, board)
  }

  private def getRightMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, 0, 1))


  private def getLeftMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, 0, -1))


  private def getTopMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, 1, 0))


  private def getDownMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, -1, 0))


  private def getRightAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, 0, 1, color)

  private def getDownAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, -1, 0, color)


  private def getLeftAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, 0, -1, color)


  private def getTopAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, 1, 0, color)

}
