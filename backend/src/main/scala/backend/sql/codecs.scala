package backend.sql


import backend.domain.auth.{EncryptedPassword, UserEmail, UserId, UserName}
import skunk._
import skunk.codec.all._
import squants.market._

object codecs {
  val userId: Codec[UserId]     = uuid.imap[UserId](UserId.apply(_))(_.value)
  val userName: Codec[UserName] = varchar.imap[UserName](UserName.apply(_))(_.value)
  val userEmail: Codec[UserEmail] = varchar.imap[UserEmail](UserEmail.apply(_))(_.value)

  val money: Codec[Money] = numeric.imap[Money](USD(_))(_.amount)

  val encPassword: Codec[EncryptedPassword] = varchar.imap[EncryptedPassword](EncryptedPassword.apply(_))(_.value)
}
