package backend.http
import cats.implicits._
import java.util.UUID

object vars {

  protected class UUIDVar[A](f : UUID => A) {
    def unapply(str: String) : Option[A] =
      Either.catchNonFatal(f(UUID.fromString(str))).toOption
  }


}
