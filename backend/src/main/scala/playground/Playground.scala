package playground
import cats.implicits._
import cats.syntax.either
import cats.syntax.EitherObjectOps
object Playground {

  trait Counter[F[_]] {
    def get: F[Int]

    def increment: F[Unit]

  }

  val a : Either[Int,Int] = 5.asLeft

  def main(args: Array[String]): Unit = {
    println("Hello")
  }

}
