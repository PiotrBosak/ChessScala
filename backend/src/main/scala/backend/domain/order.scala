package backend.domain

import backend.domain.cart.Quantity
import backend.domain.item.ItemId
import derevo.cats.{eqv, show}
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype
import squants.market.Money

import java.util.UUID
import scala.util.control.NoStackTrace

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

  @derive(show)
  case object EmptyCartError extends NoStackTrace

  @derive(show)
  sealed trait OrderOrPaymentError extends NoStackTrace {
    def cause: String
  }

  @derive(eqv, show)
  case class OrderError(cause: String)   extends OrderOrPaymentError
  case class PaymentError(cause: String) extends OrderOrPaymentError


}
