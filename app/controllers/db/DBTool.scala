package controllers.db

import play.api._
import play.api.mvc._
import models.{SimpleDB}

object DBTool extends Controller {
  
  def index = Action { implicit req =>
    //test with swingstats
    val db = SimpleDB("org.h2.Driver", "jdbc:h2:tcp://swingstats.com/~/golf", "sa", "arvydas11")
    val columns = Seq("id", "first_name", "last_name", "email")
    val rs : Seq[Map[String, String]] = db.use() { conn =>
      db.query(conn, "select * from golfer") { rs =>
        columns.map(c => c -> rs.getString(c)).toMap
      }
    }
    Ok(views.html.db.index(columns, rs))
  }
  
}