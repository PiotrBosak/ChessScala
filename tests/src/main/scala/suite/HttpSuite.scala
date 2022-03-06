//package suite

//import cats.effect.IO
//import io.circe.Encoder
//import cats.implicits.*
//import org.http4s.circe.*
//import io.circe.syntax.*
//import org.http4s.{HttpRoutes, Request, Status}
//import weaver.{Expectations, SimpleIOSuite}
//import weaver.scalacheck.Checkers
//
//import scala.util.control.NoStackTrace

//trait HttpSuite extends SimpleIOSuite with Checkers {
//
//  case object DummyError extends NoStackTrace
//
//  def expectHttpBodyAndStatus[A: Encoder](routes: HttpRoutes[IO], req: Request[IO])(
//    expectedBody: A,
//    expectedStatus: Status
//  ): IO[Expectations] =
//    routes.run(req).value.flatMap {
//      case Some(response) =>
//        response.asJson.map { json =>
//          expect.same(response.status, expectedStatus) |+|
//            expect.same(json.dropNullValues, expectedBody.asJson.dropNullValues)
//        }
//      case None => IO.pure(failure("route not found"))
//
//    }
//
//  def expectHttpStatus(routes: HttpRoutes[IO], req: Request[IO])(expectedStatus: Status): IO[Expectations] =
//    routes.run(req).value.map {
//      case Some(resp) => expect.same(resp.status, expectedStatus)
//      case None => failure("route not found")
//    }
//
//  def expectHttpFailure(routes: HttpRoutes[IO], req: Request[IO]): IO[Expectations] =
//    routes.run(req).value.attempt.map {
//      case Left(_) => success
//      case Right(_) => failure("expected a failure")
//    }
//}
