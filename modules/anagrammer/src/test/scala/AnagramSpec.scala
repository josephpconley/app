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

    "return matches from unix list" in {
      //eon, neo, one
      Anagrammer.unscramble("eno", Source.fromURL(UNIX_URL).getLines().toSeq).size must beEqualTo(3)
    }

    "scrabble tester" in {
      //random scrabble hand
      val alphabet = "abcdefghijklmnopqrstuvwxyz"
      val hand = Stream.continually(util.Random.nextInt(alphabet.size)).map(alphabet).take(7).mkString

      println(hand)
      Anagrammer.matches(hand, Source.fromURL(SCRABBLE_URL).getLines().toSeq).foreach(println)

      2 + 2 must beEqualTo(4)
    }

    "scrabble with wild tester" in {
      val hand = "ABCDEF"
      Anagrammer.matchesWithWild(hand, 1, Source.fromURL(SCRABBLE_URL).getLines().toSeq).foreach(println)


      2 + 2 must beEqualTo(4)
    }
  }
}
