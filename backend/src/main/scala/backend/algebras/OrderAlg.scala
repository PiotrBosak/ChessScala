package backend.algebras

import backend.domain.auth.UserId
import backend.domain.cart.CartItem
import backend.domain.order.{Order, OrderId, PaymentId}
import cats.data.NonEmptyList
import squants.market.Money

trait OrderAlg[F[_]] {

  def get(
         userId: UserId,
         orderId: OrderId,
         ): F[Option[Order]]

  def findById(id: UserId): F[List[Order]]

  def create(
              userId: UserId,
              paymentId: PaymentId,
              items: NonEmptyList[CartItem],
              total: Money
            ): F[OrderId]

}
