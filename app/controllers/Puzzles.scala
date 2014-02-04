package controllers

import puzzles.{WordBank, Puzzle}
import play.api.mvc.{WebSocket, Action, Controller}
import scala.io.Source
import puzzles.Puzzle._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger
import play.api.libs.iteratee.{Iteratee, Enumerator, Concurrent}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.ArrayBuffer
import play.api.libs.EventSource
import scala.concurrent.Future

/**
 * User: joe
 * Date: 12/7/13
 */
object Puzzles extends Controller{
  def solver = Action { implicit req =>
    Ok(views.html.puzzleSolver(WordBank.BANKS))
  }

  def solve = Action(parse.json) { implicit req =>
    val bank: Seq[String] = (req.body \ "name").asOpt[String].map{ name =>
      WordBank.BANKS.filter(b => b.name == name).head.words
    }.getOrElse((req.body \ "customList").as[Seq[String]])

    val reads = ((__ \ "input").read[String] and (__ \ "numWild").read[Int]).tupled
    val (input, numWild) = reads.reads(req.body).getOrElse(throw new Exception("Bad JSON"))

    val solutions = (req.body \ "mode").as[String] match {
      case "anagram" => Puzzle.unscramble(input, bank)
      case "scrabble" => Puzzle.subsetsWithWild(input, numWild, bank).toSeq.sorted
      case _ => {
        val regex = (req.body \ "regex").as[String]
        bank.filter(word => word.matches(regex))
      }
    }

    Ok(Json.toJson(solutions))
  }

//Part 2 - Convert to Streaming

//  def solveAsync = Action.async{
//    Future(Ok)
//  }
//
//  def solutions = Action {
//    //Ok.feed(EventDao.stream &> EventSource()).as(EVENT_STREAM)
//    Ok
//  }

//  def connect = WebSocket.using[String] { request =>
//    Logger.info("Someone just connected!")
//
////  Create an Enumerator that sends a message to the  client
//    val enumerator = Enumerator.fromStream(new URL("http://pzxc.com/f/posts/14/f2-33.txt").openStream())
//
//
//
//    val aggregator = Iteratee.fold[Array[String], ArrayBuffer[String]](ArrayBuffer[String]()){ (buffer, el) =>
//      buffer.appendAll(el.filter(x => Anagrammer.isAnagram(input, x)))
//      buffer
//    }
//
////  Create an Iteratee that listens for messages from the client
//    val iteratee = Iteratee.foreach[Array[Byte]] { msg =>
//      Logger.info(s"Got message $msg")
//    }
//
//    (iteratee, enumerator)
//  }
}