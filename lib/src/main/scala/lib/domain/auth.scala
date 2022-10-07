package lib.domain

object auth {

  type UserId = UserId.Type
  object UserId extends IdNewtype

  type UserName = UserName.Type
  object UserName extends Newtype[String]

  type Password = Password.Type
  object Password extends Newtype[String]


}
