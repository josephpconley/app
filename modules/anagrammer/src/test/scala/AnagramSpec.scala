import anagrams.Anagrammer
import anagrams.Anagrammer._
import org.specs2.mutable._
import scala.io.Source

/**
 * User: joe
 * Date: 12/6/13
 */
class AnagramSpec extends Specification {

  "Anagrammer" should {
    "return matches" in {
      val bank = Seq("one", "two", "three")

      Anagrammer.unscramble("eno", bank)(0) must beEqualTo("one")
    }

    "jumble" in {
      val hand = "adahe"
      Anagrammer.unscramble(hand, Source.fromURL(UNIX.url).getLines().toSeq).foreach(println)

      2 + 2 must beEqualTo(4)
    }

    "return matches from unix list" in {
      //eon, neo, one
      Anagrammer.unscramble("eno", Source.fromURL(UNIX.url).getLines().toSeq).size must beEqualTo(3)
    }

    "scrabble tester" in {
      //random scrabble hand
      val alphabet = "abcdefghijklmnopqrstuvwxyz"
      val hand = Stream.continually(util.Random.nextInt(alphabet.size)).map(alphabet).take(7).mkString

//      Anagrammer.matches(hand, Source.fromURL(SCRABBLE.url).getLines().toSeq).foreach(println)

      2 + 2 must beEqualTo(4)
    }

    "scrabble with wild tester" in {
      val hand = "ABCDEF"
//      Anagrammer.matchesWithWild(hand, 1, Source.fromURL(SCRABBLE.url).getLines().toSeq).foreach(println)

      2 + 2 must beEqualTo(4)
    }
  }
}
