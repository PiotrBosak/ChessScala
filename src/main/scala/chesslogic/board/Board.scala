package chesslogic.board

case class Board(tiles:Map[Position, Tile]) {

  def getTile(position: Position):Option[Tile] = tiles.get(position)

  def changeTile(tile:Tile):Board ={
    val newTiles = tiles + (tile.position -> tile)
    Board(newTiles)
  }

}
object Board {
  def apply(): Board = Board(BoardFactory())
}
