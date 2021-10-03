package backend.algebras
import backend.domain.category._
trait CategoryAlg[F[_]] {
  def findAll : F[List[Category]]

  def create(name : CategoryName) : F[CategoryId]

}
