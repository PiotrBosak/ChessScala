package backend.http.routes

import backend.algebras.ItemAlg
import backend.domain.category.Category
import cats.Monad
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

case class ItemRoutes[F[_] : Monad](
                                     itemAlg: ItemAlg[F]
                                   ) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/items"

  object CategoryQueryParam extends OptionalQueryParamDecoderMatcher[Category]("category")

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root :? CategoryQueryParam(category) =>
      Ok(category.fold(itemAlg.findAll)(c => itemAlg.findByCategory(c.name)))
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
