package lib.domain

type SocketId = SocketId.Type
object SocketId extends IdNewtype

type PulsarURI = PulsarURI.Type
object PulsarURI extends Newtype[String]
