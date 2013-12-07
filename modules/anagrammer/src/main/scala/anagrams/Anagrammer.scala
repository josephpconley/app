package anagrams

case class WordBank(name:String, url: String)

object Anagrammer {
  val alphabet = "abcdefghijklmnopqrstuvwxyz"

  val SCRABBLE = WordBank("Scrabble", "http://pzxc.com/f/posts/14/f2-33.txt")
  val UNIX = WordBank("UNIX", "http://www.freebsd.org/cgi/cvsweb.cgi/src/share/dict/web2?rev=1.12;content-type=text%2Fplain")

  val BANKS = Seq(SCRABBLE, UNIX)

  def unscramble(jumble: String, wordBank: Seq[String]): Seq[String] = wordBank.filter(w => w.size == jumble.size && jumble.sorted.toLowerCase == w.sorted.toLowerCase)

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
