package controllers

import anagrams.Anagrammer
import play.api.mvc.{Action, Controller}

/**
 * User: joe
 * Date: 12/7/13
 */
object Anagrams extends Controller{
  def solver = Action { implicit req =>
    Ok(views.html.anagramSolver(Anagrammer.BANKS))
  }

  def solutions = Action { implicit req =>
    Ok
  }
}
