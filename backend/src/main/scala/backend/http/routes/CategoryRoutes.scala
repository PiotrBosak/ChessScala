package backend.http.routes

import backend.algebras.CategoryAlg
import backend.domain.category.CategoryName
import backend.ext.http4s.refined.refinedQueryParamDecoder
import cats.{Applicative, Monad, MonadThrow}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Decoder
import cats.implicits._
import io.circe.Json.JString
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class CategoryRoutes[F[_] : MonadThrow](
                                                    categoryAlg: CategoryAlg[F]
                                                  ) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/categories"

  object CreateNameQueryParam extends OptionalQueryParamDecoderMatcher[String Refined NonEmpty]("name")

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(categoryAlg.findAll)
    case POST -> Root / "create" :? CreateNameQueryParam(name) =>
      name.fold(BadRequest("name is not empty")) { name =>
        Ok(categoryAlg.create(CategoryName(name)))
      }
  }
  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
