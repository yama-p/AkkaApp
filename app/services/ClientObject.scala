package services

import java.io.File

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}
import play.api.Logger
import akka.pattern.ask
import akka.util.Timeout
import services.common.{ReqData, SendData}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object ClientObject {
  implicit val timeout: Timeout = Timeout(3000 milliseconds)
  val configFile: String = getClass.getClassLoader.getResource("client_application.conf").getFile
  val config: Config = ConfigFactory.parseFile(new File(configFile))
  val system = ActorSystem("client-system", config)
  val clientActor: ActorRef = system.actorOf(Props[ClientActor], name = "client")

  def sendNumberToRemote(n: Long): Either[Throwable, String] = {
    try {
      val f = clientActor ? SendData(n)
      Await.ready(f, timeout.duration)
      f.value.get match {
        case Success(ReqData(s: String)) =>
          Logger.info(s"ClientObject : Success : Right($s)")
          Right(s)
        case Failure(e) =>
          Logger.info(s"ClientObject : Failure : Left($e)")
          Left(e)
        case _ =>
          Logger.info(s"ClientObject : ??? : Right(Unknown)")
          Right("Unknown")
      }
    } catch {
      case e: Exception =>
        Logger.info(s"ClientObject : Exception : Left($e)")
        Left(e)
    }
  }
}
