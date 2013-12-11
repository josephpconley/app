import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

class TestSpec extends Specification {
  
  "Test" should {
    
    "test" in {
      //does Play JS accept duplciate keys in JSON?
      val json = Json.parse(
        """
          |{"id": 1, "name": "Manny", "name": "Moe", "name": "Jack"}
        """.stripMargin).as[JsObject]
      println(json)

      //takes the last value of the duplicate keys when converting to map
      println(json.value)

//      val seq = Seq(1,2,3)
      val seq = JsArray(Seq(JsNumber(1), JsNumber(2), JsNumber(3)))

      1 + 1 must beEqualTo(2)
    }
  }
}