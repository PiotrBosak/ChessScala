package backend.domain

import commondomain.IsUUID
import backend.effects.GenUUID
import cats.Functor
import commondomain.IsUUID.*
import cats.syntax.functor.*

object ID {
  def make[F[_]: Functor: GenUUID, A: IsUUID]: F[A] =
    GenUUID[F].make.map(IsUUID[A].iso.get)

  def read[F[_]: Functor: GenUUID, A: IsUUID](str: String): F[A] =
    GenUUID[F].read(str).map(IsUUID[A].iso.get)
}
