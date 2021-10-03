package backend.domain

import backend.domain.category.{Category, CategoryId}
import backend.optics.uuid

import derevo.cats._
import derevo.circe.magnolia._
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype

import squants.market._
import java.util.UUID

object item {

  @derive(decoder,keyDecoder,keyEncoder, encoder, show, uuid)
  @newtype
  case class ItemId(value: UUID)

  @derive(decoder, encoder, show)
  @newtype
  case class ItemName(name: NonEmptyString)

  @derive(decoder, encoder, show)
  @newtype
  case class ItemDescription(value: String)

  @derive(decoder, encoder, show)
  case class Item(
                   id: ItemId,
                   name: ItemName,
                   description: ItemDescription,
                   price: Money,
                   category: Category
                 )

  case class CreateItem(
                         name: ItemName,
                         description: ItemDescription,
                         price: Money,
                         categoryId: CategoryId
                       )

  case class UpdateItem(
                         id: ItemId,
                         price: Money
                       )

}

