package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import services.ClientObject

@Singleton
class HomeController @Inject() extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def show(number: Long) = Action {
    ClientObject.sendNumberToRemote(number) match {
      case Right(s) => Ok(views.html.index(s"Show => $number * 2 = $s"))
      case Left(e) => Ok(views.html.index(s"Error!!! : $e"))
    }
  }
}
