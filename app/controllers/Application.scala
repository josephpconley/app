package controllers

import play.api._
import play.api.mvc._
import models.UserTable

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
}