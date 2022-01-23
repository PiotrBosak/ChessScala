package backend.algebras
import backend.domain.gamesearch._
trait GameSearchAlg[F[_]] {

  def startSearch : F[StartSearchResult]
  def poke: F[PokeResult]
  def stopSearching: F[StopSearchResult]

}
