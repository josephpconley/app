package controllers

import anagrams.Anagrammer
import play.api.mvc.{Action, Controller}
import scala.io.Source
import anagrams.Anagrammer._
import play.api.libs.json._
import play.api.libs.functional.syntax._



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
}
