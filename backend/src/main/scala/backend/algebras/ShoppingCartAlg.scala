package backend.algebras

import backend.domain.auth.UserId
import backend.domain.cart.{Cart, CartTotal, Quantity}
import backend.domain.item.ItemId

trait ShoppingCartAlg[F[_]] {

  def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def delete(userId: UserId): F[Unit]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, cart: Cart): F[Unit]
}
