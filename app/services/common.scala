package services

package object common {
  case class SendData(msg: Long)
  case class ReqData(msg: String)
}
