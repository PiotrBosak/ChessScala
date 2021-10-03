package backend.algebras

import backend.domain.order.PaymentId
import backend.domain.payment.Payment

trait PaymentClientAlg[F[_]] {

  def process(payment: Payment): F[PaymentId]

}
