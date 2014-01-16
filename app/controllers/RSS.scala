package controllers

import anagrams.Anagrammer
import play.api.mvc.{WebSocket, Action, Controller}
import scala.io.Source
import anagrams.Anagrammer._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import play.api.libs.iteratee.{Iteratee, Enumerator, Concurrent}
import java.net.URL

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ArrayBuffer
import play.api.libs.EventSource
import scala.concurrent.Future
import com.josephpconley.rss.Feed
import com.josephpconley.books.NewEBooks

/**
 * User: joe
 * Date: 12/7/13
 */
object RSS extends Controller{
  val feeds = Seq(NewEBooks.delco, NewEBooks.philly)

  def index = Action { implicit req =>
    Ok(views.html.rss(feeds))
  }

  def xml(name: String) = Action { implicit req =>
    Ok(feeds.find(f => name == f.name).head.xml)
  }
}
