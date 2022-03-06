package chesslogic.board

import io.circe.syntax.*
import cats.derived.semiauto.*
import io.circe.Codec



case class Position(row:Int,column:Int) derives Codec.AsObject
