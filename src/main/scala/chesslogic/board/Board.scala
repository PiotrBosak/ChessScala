package chesslogic.board

class Board(val tiles:Map[Position, Tile]) {
  def this() = this(BoardFactory())
}
