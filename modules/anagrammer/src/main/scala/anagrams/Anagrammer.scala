package anagrams

import scala.io.Source

case class WordBank(name:String, url: String)

object Anagrammer {
  val alphabet = "abcdefghijklmnopqrstuvwxyz"

  val SCRABBLE = WordBank("Scrabble", "http://pzxc.com/f/posts/14/f2-33.txt")
  val UNIX = WordBank("UNIX", "http://www.freebsd.org/cgi/cvsweb.cgi/src/share/dict/web2?rev=1.12;content-type=text%2Fplain")

  val BANKS = Seq(SCRABBLE, UNIX)

  def isAnagram(s1: String, s2: String) = s1.sorted.toLowerCase == s2.sorted.toLowerCase

  def unscramble(jumble: String, wordBank: Seq[String]): Seq[String] = wordBank.filter(w => isAnagram(jumble, w))

  def isSubset(s:String, letters:String) = s.toSet.subsetOf(letters.toSet)

  //for scrabble-like situations, return all valid subsets (words) from a string of letters
  def subsets(letters: String, wordBank: Seq[String]): Seq[String] = wordBank.filter(w => isSubset(w, letters))

  def subsetsWithWild(letters: String, numWild: Int, wordBank:Seq[String]): Seq[String] = {
    numWild match {
      case 0 => subsets(letters, wordBank)
      case x:Int => alphabet.flatMap(alpha => subsetsWithWild(letters + alpha, x - 1, wordBank))
    }
  }
}

//TODO how can we do this in our app?
object AnagramApp extends App{
  val bank = Source.fromURL(Anagrammer.SCRABBLE.url).getLines().toSeq
  val doubleS = bank.filter(word => word.matches(".*ss.*"))

  val pairs = doubleS.map(word => word -> word.replace("ss", "")).seq
  val validWords = pairs.filter(t => bank.contains(t._2))
  validWords.foreach(println)

}
