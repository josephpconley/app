package controllers.db

import play.api._
import play.api.mvc._
import models.{DBConfig, SimpleDB}
import play.api.libs.json.Json

object DBTool extends Controller {
  
  def index = Action { implicit req =>
    //test with swingstats
//    val db = SimpleDB("org.h2.Driver", "jdbc:h2:tcp://swingstats.com/~/golf", "sa", "arvydas11")
//    val columns = Seq("id", "first_name", "last_name", "email")
//    val rs : Seq[Map[String, String]] = db.use() { conn =>
//      db.query(conn, "select * from golfer") { rs =>
//        columns.map(c => c -> rs.getString(c)).toMap
//      }
//    }
    Ok(views.html.db.index())
  }

  def execute = Action(parse.json) { implicit req =>
    Json.reads[DBConfig].reads(req.body).fold(
      invalid => BadRequest,
      config => {
        val db = SimpleDB(config)
        (req.body \ "query").asOpt[String].map{ query =>
          val columns = Seq("id", "first_name", "last_name", "email")
          val rs : Seq[Map[String, String]] = db.use() { conn =>
            db.query(conn, query) { rs =>
              columns.map(c => c -> rs.getString(c)).toMap
            }
          }
          Ok(Json.obj("columns" -> Json.toJson(columns), "rows" -> Json.toJson(rs)))
        }.getOrElse(BadRequest)
      }
    )
  }
}