import cats.data.Validated
import cats.syntax.all.*
(
  Left("Hello"),
  Right(2),
  Left("World")
).parMapN { case (a,b,c) => (a,b,c) }

(
  Validated.Valid(5),
  Validated.Invalid("Hello"),
    Validated.Invalid("Hello")
).mapN { case (a,b,c) => (a,b,c) }