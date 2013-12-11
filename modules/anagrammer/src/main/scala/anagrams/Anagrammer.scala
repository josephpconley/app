package anagrams

case class WordBank(name:String, url: String)

object Anagrammer {
  val alphabet = "abcdefghijklmnopqrstuvwxyz"

  val SCRABBLE = WordBank("Scrabble", "http://pzxc.com/f/posts/14/f2-33.txt")
  val UNIX = WordBank("UNIX", "http://www.freebsd.org/cgi/cvsweb.cgi/src/share/dict/web2?rev=1.12;content-type=text%2Fplain")

  val BANKS = Seq(SCRABBLE, UNIX)

  def isAnagram(s1: String, s2: String) = s1.sorted.toLowerCase == s2.sorted.toLowerCase

  def unscramble(jumble: String, wordBank: Seq[String]): Seq[String] = wordBank.filter(w => isAnagram(jumble, w))

  //for scrabble-like situations, return all possible matches from a string of letters
  def matches(letters: String, wordBank: Seq[String]): Seq[String] = {
    val combos:Seq[Set[Char]] = letters.toCharArray.toSet.subsets.toSeq
    combos.flatMap(c => unscramble(c.mkString, wordBank))
  }

  def matchesWithWild(letters: String, numWild: Int, wordBank:Seq[String]): Seq[String] = {
    numWild match {
      case 0 => matches(letters, wordBank)
      case x:Int => alphabet.flatMap(alpha => matchesWithWild(letters + alpha, x - 1, wordBank))
    }
  }
}
