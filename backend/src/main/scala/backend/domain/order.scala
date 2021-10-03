package backend.domain

import backend.domain.cart.Quantity
import backend.domain.item.ItemId
import derevo.cats.show
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.market.Money

import java.util.UUID

object order {
  @derive(decoder, encoder, show)
  @newtype
  case class OrderId(value: UUID)

  @derive(decoder, encoder, show)
  @newtype
  case class PaymentId(value: UUID)

  @derive(decoder,encoder)
  case class Order(
                  id: OrderId,
                  pid: PaymentId,
                  items: Map[ItemId, Quantity],
                  total: Money
                  )




}
