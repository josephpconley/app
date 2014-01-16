import play.api._

import play.api.libs.concurrent.Akka
import play.api.mvc.{Results, RequestHeader}

import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart (app: Application){
//    println("Generating DDL")
//    io.SourceFileUtils.writeStringToFile(new java.io.File("slick.sql"),
//    Models.ddl.dropStatements.mkString("\n", ";\n\n", ";\n") + Models.ddl.createStatements.mkString("\n", ";\n\n", ";\n"));

    //update RSS feeds every 8 hours
//    Akka.system.scheduler.schedule(0 seconds, 8 hours){
//
//    }
  }
}