package backend

import cats.syntax.contravariant._
import cats.{Eq, Monoid, Show}
import chesslogic.board.Tile
import chesslogic.board.position.Position
import dev.profunktor.auth.jwt.JwtToken
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.types.string.NonEmptyString
import io.circe._
import squants.market.{Currency, Money, USD}

package object domain extends OrphanInstances

// instances for types we don't control
trait OrphanInstances {


 implicit val moneyDecoder: Decoder[Money] =
    Decoder[BigDecimal].map(USD.apply)

  implicit val moneyEncoder: Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)

  implicit val keyPositionEncoder: KeyEncoder[Position] = KeyEncoder.instance(Encoder[Position].apply(_).noSpaces)
  implicit val keyPositionDecoder: KeyDecoder[Position] = KeyDecoder.instance(j => Decoder[Position].decodeJson(Json.fromString(j)).toOption)
  implicit val encodeMap = Encoder.encodeMap[Position,Tile]
  implicit val decodeMap = Decoder.decodeMap[Position,Tile]

  implicit val nonNegativeDecoder: Decoder[Int Refined NonNegative] =
    Decoder[Int].emap(i => if (i >= 0) Right(Refined.unsafeApply[Int,NonNegative](i)) else Left("Passed number is negative"))

  implicit val nonNegativeEncoder: Encoder[Int Refined NonNegative] =
    Encoder[Int].contramap[Int Refined NonNegative](n => n.value)

  implicit val nonEmptyStringEncoder: Encoder[String Refined NonEmpty] =
    Encoder[String].contramap(_.value)

  implicit val nonEmptyStringDecoder: Decoder[String Refined NonEmpty] =
    Decoder[String].emap(s => if(s.isEmpty) Left("name cannot be empty") else Right(Refined.unsafeApply[String,NonEmpty](s)))

  implicit val nonEmptyStringShow : Show[String Refined NonEmpty] =
    Show.fromToString[NonEmptyString]


  implicit val nonNegativeShow : Show[Int Refined NonNegative] =
    Show[Int].contramap(_.value)

  implicit val moneyMonoid: Monoid[Money] =
    new Monoid[Money] {
      def empty: Money = USD(0)

      def combine(x: Money, y: Money): Money = x + y
    }

  implicit val currencyEq: Eq[Currency] = Eq.and(Eq.and(Eq.by(_.code), Eq.by(_.symbol)), Eq.by(_.name))

  implicit val moneyEq: Eq[Money] = Eq.and(Eq.by(_.amount), Eq.by(_.currency))

  implicit val moneyShow: Show[Money] = Show.fromToString

  implicit val tokenEq: Eq[JwtToken] = Eq.by(_.value)

  implicit val tokenShow: Show[JwtToken] = Show[String].contramap[JwtToken](_.value)

  implicit val tokenEncoder: Encoder[JwtToken] =
    Encoder.forProduct1("access_token")(_.value)
}
