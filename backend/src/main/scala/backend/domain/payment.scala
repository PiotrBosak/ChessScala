package backend.domain

import backend.domain.auth.UserId
import backend.domain.checkout.Card
import derevo.cats._
import derevo.circe.magnolia.encoder
import derevo.derive
import squants.market.Money

object payment {

  @derive(encoder, show)
  case class Payment(
      id: UserId,
      total: Money,
      card: Card
  )

}
