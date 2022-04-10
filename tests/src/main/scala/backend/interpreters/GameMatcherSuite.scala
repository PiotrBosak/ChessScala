package backend.interpreters

import cats.effect.IO
import weaver.SimpleIOSuite
import weaver.scalacheck.Checkers
import io.odin.Logger

object GameMatcherSuite extends SimpleIOSuite with Checkers {

  given lg: Logger[IO] = Logger.noop

}
