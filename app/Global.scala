import java.io.{File, PrintWriter}

import controllers.RSS
import play.api.Play.current
import play.api._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration._

object Global extends GlobalSettings {

  override def onStart (app: Application){

    if(Play.isProd(app)){
      Akka.system.scheduler.schedule(0 seconds, 12 hours){
        Logger.info("Updating feeds")
        RSS.feeds.foreach{ f =>
          Logger.info("Updating feed " + f.name)

          val writer: PrintWriter = new PrintWriter(new File("public/feeds/" + f.name + ".xml"))
          scala.xml.XML.write(writer, f.xml(f.items()), "utf-8", true, null)
          writer.flush
        }
      }
    }
  }
}