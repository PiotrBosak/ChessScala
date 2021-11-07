package backend.programs

import backend.domain.auth.UserId
import scala.concurrent.duration._
import backend.domain.cart._
import backend.domain.checkout._
import backend.domain.order._
import backend.domain.payment._
import backend.effects.Background
import backend.http.clients.PaymentClient
import backend.retries.{Retriable, Retry}
import backend.algebras._

import cats.MonadThrow
import cats.data.NonEmptyList
import cats.syntax.all._
import org.typelevel.log4cats.Logger
import retry._
import squants.market.Money

final case class Checkout[F[_] : Background : Logger : MonadThrow : Retry](
                                                                            payments: PaymentClient[F],
                                                                            cart: ShoppingCartAlg[F],
                                                                            orders: OrderAlg[F],
                                                                            policy: RetryPolicy[F]
                                                                          ) {
  private def processPayment(in: Payment): F[PaymentId] =
    Retry[F]
      .retry(policy, Retriable.Payments)(payments.process(in))
      .adaptError {
        case e =>
          PaymentError(Option(e.getMessage).getOrElse("Unknown"))
      }

  private def createOrder(
                           userId: UserId,
                           paymentId: PaymentId,
                           items: NonEmptyList[CartItem],
                           total: Money
                         ): F[OrderId] = {
    val action =
      Retry[F]
        .retry(policy, Retriable.Orders)(orders.create(userId, paymentId, items, total))
        .adaptError {
          case e => OrderError(e.getMessage)
        }

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.onError {
        case _ =>
          Logger[F].error(
            s"Failed to create order for Payment: ${paymentId.show}. Rescheduling as a background action"
          ) *>
            Background[F].schedule(bgAction(fa), 1.hour)
      }

    bgAction(action)
  }

  private def ensureNonEmpty[A](xs: List[A]): F[NonEmptyList[A]] =
    MonadThrow[F].fromOption(NonEmptyList.fromList(xs), EmptyCartError)

  def process(userId: UserId, card: Card): F[OrderId] =
    cart.get(userId).flatMap {
      case CartTotal(items, total) =>
        for {
          its <- ensureNonEmpty(items)
          pid <- processPayment(Payment(userId, total, card))
          oid <- createOrder(userId, pid, its, total)
          _   <- cart.delete(userId).attempt.void
        } yield oid
    }


}
