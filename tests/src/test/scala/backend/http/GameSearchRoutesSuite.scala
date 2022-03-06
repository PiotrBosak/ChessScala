//package backend.http
//
//import backend.algebras.GameSearchAlg
//import backend.domain.game.GameId
//import backend.domain.gamesearch.{GameFound, SearchStartSuccessful, SearchStopSuccessful}
//import backend.domain.{ID, auth, gamesearch}
//import cats.effect.IO
//import suite.HttpSuite

//class GameSearchRoutesSuite extends HttpSuite {
//
//
////  test("POST start search successful") {
////    forall(Gen.list)
////  }
//}
//
//protected class TestGameSearch extends GameSearchAlg[IO] {
//  override def startSearch(userId: auth.UserId): IO[gamesearch.StartSearchResult] = IO.pure(SearchStartSuccessful)
//
//  override def poll(userId: auth.UserId): IO[gamesearch.PokeResult] = ID.make[IO,GameId].map(GameFound.apply)
//
//  override def stopSearching(userId: auth.UserId): IO[gamesearch.StopSearchResult] = IO.pure(SearchStopSuccessful)
//}
//
