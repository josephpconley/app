package models

import slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import play.api.db.DB
import play.api.Play.current

import scala.slick.lifted.{DDL, MappedTypeMapper}
import org.joda.time.DateTime
import java.sql.Timestamp
import scala.slick.jdbc.meta.MTable
import scala.slick.driver.BasicTableComponent
import scala.Predef.String

/**
 * User: josep_000
 * Date: 7/25/13
 */

object Models{
  implicit def time2dateTime = MappedTypeMapper.base[DateTime, Timestamp] (
    dateTime => new Timestamp(dateTime.getMillis),
    time => new DateTime(time.getTime)
  )

  def flatten3[A, B, C](t: ((A, B), C)) : (A, B, C) = (t._1._1, t._1._2, t._2)
  def flatten4[A, B, C, D](t: (((A, B), C), D)) : (A, B, C, D) = (t._1._1._1, t._1._1._2, t._1._2, t._2)
  def flatten5[A, B, C, D, E](t: ((((A, B), C), D), E)) : (A, B, C, D, E) = (t._1._1._1._1, t._1._1._1._2, t._1._1._2, t._1._2, t._2)

  def test = {
    Database.forDataSource(DB.getDataSource()) withTransaction {
      val tableList = MTable.getTables(None, Some("public"), None, None).list.map(t => (t.name.name, t.name.schema))
    }
  }
}