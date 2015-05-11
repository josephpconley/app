package controllers

import play.api.mvc.{Action, Controller}
import com.josephpconley.books.NewEBookFeed
import java.io.File

object RSS extends Controller{
  val feeds = Seq(NewEBookFeed.delco, NewEBookFeed.philly)

  def index = Action { implicit req =>
    Ok(views.html.rss(feeds))
  }

  def xml(name: String) = Action { implicit req =>
    feeds.find(f => name == f.name).headOption.map{ feed =>
      Ok(scala.xml.XML.loadFile("public/feeds/" + name + ".xml"))
    }.getOrElse(BadRequest("Feed not found: " + name))
  }
}
