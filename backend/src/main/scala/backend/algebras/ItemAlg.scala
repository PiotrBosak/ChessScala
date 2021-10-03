package backend.algebras

import backend.domain.category.CategoryName
import backend.domain.item._

trait ItemAlg[F[_]] {
  def findAll: F[List[Item]]

  def findByCategory(category: CategoryName): F[List[Item]]

  def findById(id: ItemId): F[Option[Item]]

  def create(item: CreateItem): F[ItemId]

  def update(item: UpdateItem): F[Unit]
}
