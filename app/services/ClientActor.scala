package services

import play.api.Logger
import akka.actor.{Actor, ActorSelection}
import akka.pattern.ask
import akka.util.Timeout
import services.common.{SendToClient, SendToRemote}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class ClientActor extends Actor {
  val remoteActor: ActorSelection = context.actorSelection(ClientObject.config.getString("app.remote-app.remote-actor"))
  implicit val timeout: Timeout = Timeout(2000 milliseconds)

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info(s"Remote actor is $remoteActor")
  }

  override def receive: Receive = {
    case SendToClient(send: Long) =>
      try {
        Logger.info(s"$sender")
        val localSender = sender
        val f = remoteActor ? SendToRemote(send) // ASK
        f.onComplete {
          case Success(s) =>
            Logger.info(s"$sender")
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
