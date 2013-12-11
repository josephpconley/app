package models.db

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json

import com.log4p.sqldsl.SQLParser

/**
 * User: jconley
 * Date: 11/10/13
 */
class SQLParserSpec extends Specification{
  "Parser" should {
    "test parser" in {
      val parser = new SQLParser()
      val stmt = parser.parse("select * from golfer")
      println(stmt)

      1 + 1 must beEqualTo(2)
    }
  }

}
