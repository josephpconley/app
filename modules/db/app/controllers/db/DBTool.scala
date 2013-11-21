package controllers.db

import play.api._
import play.api.mvc._
import models.{DBConfig, SimpleDB}
import play.api.libs.json.Json

object DBTool extends Controller {
  
  def execute = Action(parse.json) { implicit req =>
    Json.reads[DBConfig].reads(req.body).fold(
      invalid => BadRequest,
      config => {
        val db = SimpleDB(config)
        (req.body \ "query").asOpt[String].map{ query =>
          var columns:Seq[String] = Seq()
          try{
            if(query.trim.startsWith("select")){
              val rs : Seq[Map[String, String]] = db.use() { conn =>
                db.query(conn, query) { rs =>
                  val meta = rs.getMetaData
                  columns = for(i <- 1 to meta.getColumnCount) yield meta.getColumnName(i)
                  columns.map(c => c -> rs.getString(c)).toMap
                }
              }
              if(rs.isEmpty){
                db.use(){ conn =>

                }
              }

              Ok(Json.obj("columns" -> Json.toJson(columns), "rows" -> Json.toJson(rs)))
            }else{
              db.use(){ conn =>
                db.execute(conn, query)
                Ok(Json.obj("message" -> "Query executed successfully."))
              }
            }
          }catch{
            case e:Exception => BadRequest(Json.obj("message" -> e.getMessage))
          }

        }.getOrElse(BadRequest)
      }
    )
  }
}