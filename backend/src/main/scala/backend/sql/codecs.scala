package backend.sql

import backend.domain.auth.{ Email, EncryptedPassword, UserId, UserName }
import skunk.*
import skunk.codec.all.*
import squants.market.*

object codecs {
  val userId: Codec[UserId]     = uuid.imap[UserId](UserId(_))(_.value)
  val userName: Codec[UserName] = varchar.imap[UserName](UserName(_))(_.value)
  val email: Codec[Email]       = varchar.imap[Email](Email(_))(_.value)

  val money: Codec[Money] = numeric.imap[Money](USD(_))(_.amount)

  val encPassword: Codec[EncryptedPassword] = varchar.imap[EncryptedPassword](EncryptedPassword(_))(_.value)
}
