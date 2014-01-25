import controllers.RSS
import java.io.{PrintWriter, File}
import play.api._

import play.api.libs.concurrent.Akka
import play.api.mvc.{Results, RequestHeader}

import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import play.api.Play.current
import scalax.io.Resource

object Global extends GlobalSettings {

  override def onStart (app: Application){
//    println("Generating DDL")
//    io.SourceFileUtils.writeStringToFile(new java.io.File("slick.sql"),
//    Models.ddl.dropStatements.mkString("\n", ";\n\n", ";\n") + Models.ddl.createStatements.mkString("\n", ";\n\n", ";\n"));

    //update RSS feeds every 6 hours
    if(Play.isProd(app)){
      Akka.system.scheduler.schedule(0 seconds, 12 hours){
        Logger.info("Updating feeds")
        RSS.feeds.foreach{ f =>
          Logger.info("Updating feed " + f.name)

          val writer: PrintWriter = new PrintWriter(new File("public/feeds/" + f.name + ".xml"))
          scala.xml.XML.write(writer, f.xml, "utf-8", true, null)
          writer.flush
        }
      }
    }
  }
}