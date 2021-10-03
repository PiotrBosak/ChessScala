package backend.algebras

import backend.domain.cart.Quantity
import backend.domain.item.ItemId
import backend.domain.user.UserId

trait ShoppingCartAlg[F[_]] {

  def add(
         userId: UserId,
         itemId: ItemId,
         quantity: Quantity
         ): F[Unit]
}
