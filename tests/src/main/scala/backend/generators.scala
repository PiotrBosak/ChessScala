package backend

import backend.domain.auth.UserId
import backend.domain.game.{GameId, PvPGame}
import chesslogic.board.Board
import chesslogic.game.FullGame.Turn
import chesslogic.game.FullGame.Turn.{BlackTurn, WhiteTurn}
import chesslogic.game.SimpleGame
import org.scalacheck.Gen

import java.util.UUID

object generators {

  val nonEmptyStringGen: Gen[String] =
    Gen
      .chooseNum(21,40)
      .flatMap { n =>
        Gen.buildableOfN[String,Char](n, Gen.alphaChar)
      }

  def nesGen[A](f: String => A): Gen[A] =
    nonEmptyStringGen.map(f)

  def idGen[A](f: UUID => A): Gen[A] =
    Gen.uuid.map(f)

  val gameIdGen: Gen[GameId] =
    idGen(GameId.apply(_))

  val boardGen: Gen[Board] =
    Gen.oneOf(List(Board()))

  val turnGen: Gen[Turn] =
    Gen.oneOf(List(WhiteTurn,BlackTurn))

  val simpleGameGen: Gen[SimpleGame] =
    for {
      board <- boardGen
      turn <- turnGen
    } yield SimpleGame(board,turn)

  val userIdGen: Gen[UserId] =
    Gen.uuid.map(UserId.apply(_))

  val pvpGame: Gen[PvPGame] =
    for {
      whitePlayer <- userIdGen
      blackPlayer <- userIdGen
      gameId <- gameIdGen
      game <- simpleGameGen
    } yield PvPGame(whitePlayer,blackPlayer,gameId,game)


}
