package controllers

import play.api._
import play.api.mvc._
import models.{User, UserTable}
import play.api.data.Form
import play.api.data.Forms._
import anagrams.Anagrammer
import java.sql.Date

object Application extends Controller {

  def index = Action { implicit req =>
    Ok(views.html.index())
  }

  def api = Action { implicit req =>
    Ok(views.html.api())
  }

  def db = Action { implicit req =>
    Ok(views.html.db())
  }

  def users = Action { implicit req =>
    val joe = User("joe", "conley", new Date(new java.util.Date().getTime))
    UserTable.insert(joe)
    Ok(UserTable.findAll.mkString("\n"))
  }
}