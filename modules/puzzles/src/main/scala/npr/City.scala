package npr

import scala.io.Source

/**
 * User: joe
 * Date: 12/9/13
 *
 * http://puzzles.blainesville.com/2013/12/npr-sunday-puzzle-dec-8-2013-this-city.html
 */

object City extends App{
  println("searching all nine letter U.S. cities")

  val alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray

  def shift(origChar: Char, shift: Int) = alphabet( (alphabet.indexOfSlice(Seq(origChar)) + shift) % 26 )

  val validCities = Source.fromInputStream(getClass.getResourceAsStream("../cities.txt")).getLines().filter(city => !city.contains(" ") && city.length == 9)
  validCities.foreach{ c =>
    val city = c.toCharArray

    //perform shifts per the puzzle specs
    city.update(2, shift(city(2), 6))
    city.update(8, shift(city(8), 7))

    //print out potentials
    println(c + " " + city.mkString)
  }
}