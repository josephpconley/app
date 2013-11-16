package models.db

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import models.SimpleDB
import com.log4p.sqldsl.SQLParser

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class SimpleDBSpec extends Specification {
  
  "Application" should {

    "test h2 mem db" in {
      val db = SimpleDB("org.h2.Driver", "jdbc:h2:mem:test", "sa", "")
      val rs : Seq[(Long, Int)] = db.use(autoCommit = false) { conn =>
        db.execute(conn, "create table a (id int primary key auto_increment, num int)")
        db.update(conn, "insert into a(num) values(?)", Seq(Seq(1)))
        db.query(conn, "select * from a") { rs: java.sql.ResultSet =>
          rs.getLong("id") -> rs.getInt("num")
        }
      }

      rs must beEqualTo(Seq((1,1)))
    }

    "generic result map" in {
      val db = SimpleDB("org.h2.Driver", "jdbc:h2:mem:test", "sa", "")
      val rs : Seq[Map[String, String]] = db.use(autoCommit = false) { conn =>
        db.execute(conn,
          """
            |create table a(
            | id int primary key auto_increment,
            | num int,
            | price double,
            | str varchar(10),
            | last date default now()
            |)
          """.stripMargin)
        db.update(conn, "insert into a(num, price, str) values(1, 9.99, 'Joe')")
        db.update(conn, "insert into a(num, price, str) values(2, 19.99, 'Conley')")
        val columns = Seq("id", "num", "price", "str", "last")
        db.query(conn, "select * from a") { rs: java.sql.ResultSet =>
          columns.map(c => c -> rs.getString(c)).toMap
        }
      }

      rs.size must beEqualTo(2)
      rs(0).get("num").get must beEqualTo("1")
    }
  }
}