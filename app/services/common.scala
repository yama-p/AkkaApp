package services

package object common {
  case class SendToClient(msg: Long)
  case class SendToRemote[A](msg: A)
  case class ResponseMsg(msg: String)
}
