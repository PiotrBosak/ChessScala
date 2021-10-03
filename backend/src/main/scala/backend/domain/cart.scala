package backend.domain
import backend.domain.item.{Item, ItemId}

import java.util.UUID
import backend.optics._
import derevo.cats.show
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import derevo.cats._
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{NonNegative, Positive}
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import squants.market.Money
object cart {

  @derive(decoder,encoder,show)
  @newtype
  case class Quantity(value: Int Refined NonNegative)

  @derive(encoder,decoder,show)
  @newtype
  case class Cart(items: Map[ItemId, Quantity])
  object Cart {
    implicit val jsonEncoder : Encoder[Cart] =
      Encoder.forProduct1("items")(_.items)

    implicit val jsonDecoder : Decoder[Cart] =
      Decoder.forProduct1("items")(Cart.apply)
  }

  case class CartItem(item: Item, quantity: Quantity)
  case class CartTotal(items: List[CartItem], total: Money)



}
