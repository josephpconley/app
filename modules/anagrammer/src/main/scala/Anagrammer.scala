/**
 * User: joe
 * Date: 12/6/13
 */
object Anagrammer {
  val alphabet = "abcdefghijklmnopqrstuvwxyz"

  val SCRABBLE_URL = "http://pzxc.com/f/posts/14/f2-33.txt"
  val UNIX_URL = "http://www.freebsd.org/cgi/cvsweb.cgi/src/share/dict/web2?rev=1.12;content-type=text%2Fplain"


  def unscramble(jumble: String, dictionary: Seq[String]): Seq[String] = dictionary.filter(w => w.size == jumble.size && jumble.sorted.toLowerCase == w.sorted.toLowerCase)

  def matches(letters: String, dictionary: Seq[String]): Seq[String] = {
    val combos:Seq[Set[Char]] = letters.toCharArray.toSet.subsets.toSeq
    combos.flatMap(c => unscramble(c.mkString, dictionary))
  }

  def matchesWithWild(letters: String, numWild: Int, dictionary:Seq[String]): Seq[String] = {
    numWild match {
      case 0 => matches(letters, dictionary)
      case x:Int => alphabet.flatMap(alpha => matchesWithWild(letters + alpha, x - 1, dictionary))
    }
  }
}
