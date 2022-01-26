package chesslogic.board

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

@derive(encoder,decoder)
case class Position(row:Int,column:Int)
