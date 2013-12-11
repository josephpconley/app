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

/**
 * User: joe
 * Date: 12/7/13
 */
object Anagrams extends Controller{
  def solver = Action { implicit req =>
    Ok(views.html.anagramSolver(Anagrammer.BANKS))
  }

  def solve = Action(parse.json) { implicit req =>
    val bank: Seq[String] = (req.body \ "url").asOpt[String].map{ url =>
      Source.fromURL(url).getLines().toSeq
    }.getOrElse((req.body \ "customList").as[Seq[String]])

    val reads = ((__ \ "input").read[String] and (__ \ "numWild").read[Int]).tupled
    val (input, numWild) = reads.reads(req.body).getOrElse(throw new Exception("Bad JSON"))

    val solutions = (req.body \ "mode").as[String] match {
      case "jumble" => Anagrammer.unscramble(input, bank)
      case _ => Anagrammer.matchesWithWild(input, numWild, bank)
    }

    Ok(Json.toJson(solutions))
  }

  def connect = WebSocket.using[Array[Byte]] { request =>
    Logger.info("Someone just connected!")

//  Create an Enumerator that sends a message to the  client
    val enumerator = Enumerator.fromStream(new URL("http://pzxc.com/f/posts/14/f2-33.txt").openStream())

//  Create an Iteratee that listens for messages from the client
    val iteratee = Iteratee.foreach[Array[Byte]] { msg =>
      Logger.info(s"Got message $msg")
    }

    (iteratee, enumerator)
  }
}
