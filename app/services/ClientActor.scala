package services

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorSelection}
import play.api.Logger
import akka.pattern.ask
import akka.util.Timeout
import services.common.SendData

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class ClientActor extends Actor with ActorLogging {
  val remoteActor: ActorSelection = context.actorSelection(ClientObject.config.getString("app.remote-system.remote-actor"))
  implicit val timeout: Timeout = Timeout(2000 milliseconds)

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info(s"Remote actor is $remoteActor")
  }

  override def receive: Receive = {
    case SendData(send: Long) =>
      try {
        val localSender = sender
        val f = remoteActor ? SendData(send)
        f.onComplete {
          case Success(s) =>
            Logger.info(s"ClientActor : Success : $s")
            localSender ! s
          case Failure(e) =>
            Logger.info(s"ClientActor : Failure : $e")
            throw e
        }
        Await.ready(f, timeout.duration)
      } catch  {
        case e: Exception =>
          Logger.info(s"ClientActor : Exception : $e")
          throw e
      }

    case any =>
      Logger.info(s"Client received unknown message $any from $sender")
  }
}
