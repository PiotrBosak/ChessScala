package backend.domain

import backend.optics.uuid

import scala.util.control.NoStackTrace
import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.refined._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype

import java.util.UUID

object category {

  @derive(decoder,encoder,show,uuid)
  @newtype
  case class CategoryId(value : UUID)

  @derive(decoder,encoder,show)
  @newtype
  case class CategoryName(name : NonEmptyString)

  @derive(decoder,encoder,show)
  case class Category(id : CategoryId, name : CategoryName)


}
