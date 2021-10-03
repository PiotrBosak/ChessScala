package chesslogic.rules

import chesslogic.Color
import chesslogic.board.{Board, Position}
import chesslogic.pieces.Bishop
import chesslogic.rules.RulesForMultiTileMoves.{getViablePositionForAttacks, getViablePositionsForMoves}

 object BishopRules extends MovingRules[Bishop] {

  override def getPossibleAttacks(position: Position, board: Board): List[Position] = {
    (for {
      tile <- board.getTile(position)
      piece <- tile.currentPiece
    } yield List(
      getRightTopAttack(board, position, piece.color),
      getLeftTopAttack(board, position, piece.color),
      getRightDownAttack(board, position, piece.color),
      getLeftDownAttack(board, position, piece.color)).collect { case Some(position) => position}
      ).getOrElse(Nil)
  }

  override def getPossibleMoves(position: Position, board: Board): List[Position] =
      getLeftDownMoves(position, board) ++ getRightDownMoves(position, board) ++
      getLeftTopMoves(position, board) ++ getRightTopMoves(position, board)

  private def getRightTopMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, 1, 1))


  private def getLeftTopMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, 1, -1))


  private def getRightDownMoves(position: Position, board: Board): List[Position] =
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, -1, 1))


  private def getLeftDownMoves(position: Position, board: Board): List[Position] = {
    board.getTile(position).fold(List.empty[Position])(tile => getViablePositionsForMoves(board, tile.position, -1, -1))
  }


  private def getRightTopAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, 1, 1, color)

  private def getRightDownAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, -1, 1, color)


  private def getLeftTopAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, 1, -1, color)


  private def getLeftDownAttack(board: Board, position: Position, color: Color): Option[Position] =
    getViablePositionForAttacks(board, position, -1, -1, color)



}
