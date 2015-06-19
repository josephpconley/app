package controllers

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import puzzles.{Puzzle, WordBank}

object Puzzles extends Controller {
  def solver = Action { implicit req =>
    Ok(views.html.puzzleSolver(WordBank.BANKS))
  }

  def solve = Action(parse.json) { implicit req =>
    val bank: Seq[String] = (req.body \ "name").asOpt[String].map { name =>
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
}