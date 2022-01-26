package chesslogic

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

@derive(encoder,decoder)
sealed trait Color

case object White extends Color

case object Black extends Color
