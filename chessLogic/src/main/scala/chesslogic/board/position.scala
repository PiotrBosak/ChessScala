package chesslogic.board

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive

import scala.util.Try

object position {
  @derive(encoder, decoder)
  case class Position(file: File, rank: Rank)

  @derive(encoder, decoder)
  sealed trait Rank {

    def toNumber: Int = this match {
      case Rank.One => 1
      case Rank.Two => 2
      case Rank.Three => 3
      case Rank.Four => 4
      case Rank.Five => 5
      case Rank.Six => 6
      case Rank.Seven => 7
      case Rank.Eight => 8
    }

    def advanceUnsafe(n: Int): Rank = Rank.fromIntUnsafe(toNumber + n)

    def advance(n: Int): Option[Rank] = Try(Rank.fromIntUnsafe(toNumber + n)).toOption
  }

  object Rank {
    case object One extends Rank

    case object Two extends Rank

    case object Three extends Rank

    case object Four extends Rank

    case object Five extends Rank

    case object Six extends Rank

    case object Seven extends Rank

    case object Eight extends Rank

    def fromIntUnsafe(rank: Int): Rank = rank match {
      case 1 => One
      case 2 => Two
      case 3 => Three
      case 4 => Four
      case 5 => Five
      case 6 => Six
      case 7 => Seven
      case 8 => Eight
      case _ => throw new RuntimeException("invalid rank")
    }
  }

  @derive(encoder, decoder)
  sealed trait File {

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

    def advanceUnsafe(n: Int): File = File.fromIntUnsafe(toNumber + n)

    def advance(n: Int): Option[File] = Try(File.fromIntUnsafe(toNumber + n)).toOption

  }

  object File {
    case object A extends File

    case object B extends File

    case object C extends File

    case object D extends File

    case object E extends File

    case object F extends File

    case object G extends File

    case object H extends File


    def fromIntUnsafe(file: Int): File = file match {
      case 1 => A
      case 2 => B
      case 3 => C
      case 4 => D
      case 5 => E
      case 6 => F
      case 7 => G
      case 8 => H
      case _ => throw new RuntimeException("invalid file")
    }
  }

  @derive(encoder, decoder)
  final case class Move(from: Position, to: Position, moveType: MoveType)

  @derive(encoder, decoder)
  sealed trait MoveType

  object MoveType {
    case object Normal extends MoveType

    case object TwoTileMove extends MoveType

    case object Castling extends MoveType
    case object LePassant extends MoveType
  }
}