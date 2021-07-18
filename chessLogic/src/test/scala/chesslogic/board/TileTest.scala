package chesslogic.board

import akka.testkit.ImplicitSender
import chesslogic.{Black, White}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{BeforeAndAfterAll, TestSuite}

class TileTest extends AnyFlatSpec
with BeforeAndAfterAll{

  "Black A2 tile" should "return None" in {
      assert(Tile(Black, Position(2, 1), None).isEmpty)
    }


  "White A2 tile" should "be created" in {
    assert(Tile(White,Position(2,1),None).isDefined)
  }



  }


