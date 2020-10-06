package chesslogic.board

class Board private (val tiles:Map[Position, Tile]) {
  def this() = this(BoardFactory())
}
