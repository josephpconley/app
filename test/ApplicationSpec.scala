package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.iteratee.{Enumeratee, Iteratee, Enumerator, Traversable}
import java.net.URL
import play.api.Logger
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import anagrams.Anagrammer
import scala.collection.mutable.ArrayBuffer
import scalax.io.{Output, Resource}
import java.io.File

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
  "Application" should {

    "test async" in {
//      println(Source.fromFile(new java.io.File("conf/scrabble.txt")).getLines().size)
//      val producer = Enumerator.fromFile(new java.io.File("conf/scrabble.txt"))
      val producer = Enumerator.fromStream(new URL("http://pzxc.com/f/posts/14/f2-33.txt").openStream())

      //return all anagrams of eno
      val input = "eno"
      val aggregator = Iteratee.fold[Array[String], ArrayBuffer[String]](ArrayBuffer[String]()){ (buffer, el) =>
        buffer.appendAll(el.filter(x => Anagrammer.isAnagram(input, x)))
        buffer
      }

      val encoder: Enumeratee[Array[Byte], Array[Char]] = util.Encoding.decode()
      val adapter: Enumeratee[Array[Char], Array[String]] = Enumeratee.map[Array[Char]](a => a.mkString.split("\n"))
      val filters: Enumeratee[Array[Byte], Array[String]] = encoder.compose(adapter)

      //testing with String
      val strEncoder: Enumeratee[Array[Byte], Array[String]] = util.Encoding.decodeStrings()

//      val adapter: Enumeratee[Array[Byte], String]

//      val charPrinter = Iteratee.foreach[Array[Char]]{ a =>
//        println(a.mkString(","))
//        println(a(a.length-7) == '\n')
//        println("JOE")
//        Thread.sleep(4000)
//      }

//      new File("results.txt").delete
//      val output:Output = Resource.fromFile("results.txt")
//
//      val strPrinter = Iteratee.foreach[Array[String]]{ a =>
////        a.foreach(println)
////        println("JOE")
//
//        output.writeStrings(a, "\n")
//        output.write("JOE")
//
//        Thread.sleep(2000)
//      }

      //      new File("results.txt").delete

      val consumer: Iteratee[Array[Byte], ArrayBuffer[String]] = filters.transform(aggregator)

//      Helpers.await(producer(encoder.transform(charPrinter)).flatMap(i => i.run))
//      Helpers.await(producer(strEncoder.transform(strPrinter)).flatMap(i => i.run))

      val words = Helpers.await(producer(consumer).flatMap(i => i.run))
      words.foreach(println)

      1 + 1 must beEqualTo(2)
    }
  }
}