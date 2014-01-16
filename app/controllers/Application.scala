package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import anagrams.Anagrammer
import java.sql.Date
import models.User
import models.Role
import scala.slick.lifted.Query
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.DB

import Database.threadLocalSession

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

  def roles = Action { implicit req =>
    RoleTable.insert(Role("joe"))

  //dynamic filter
    val name = Models.db withTransaction {
      Query(RoleTable).where(_.id === 1.toLong).map(r => r.column[String]("NAME")).first
    }
    println(name)

    Ok(RoleTable.findAll.mkString("\n"))
  }
}