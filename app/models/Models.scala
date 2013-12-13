package models

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.DB

import Database.threadLocalSession

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

/**
 * Helper for otherwise verbose Slick model definitions
 */
trait CRUD[T <: AnyRef { val id: Option[Long] }] { self: Table[T] =>

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def * : scala.slick.lifted.ColumnBase[T]
  def autoInc = * returning id

  def insert(entity: T) = Models.db.withTransaction {
    autoInc.insert(entity)
  }

  def insertAll(entities: Seq[T]) = Models.db.withTransaction {
    autoInc.insertAll(entities: _*)
  }

  def findAll = Models.db.withTransaction {
    Query(this).list()
  }

  def update(id: Long, entity: T) = Models.db.withTransaction {
    tableQueryToUpdateInvoker(
      tableToQuery(this).where(_.id === id)
    ).update(entity)
  }

  def delete(id: Long) = Models.db.withTransaction {
    queryToDeleteInvoker(
      tableToQuery(this).where(_.id === id)
    ).delete
  }

  def count = Models.db.withTransaction {
    Query(tableToQuery(this).length).first
  }
}

object Models{
  implicit def time2dateTime = MappedTypeMapper.base[DateTime, Timestamp] (
    dateTime => new Timestamp(dateTime.getMillis),
    time => new DateTime(time.getTime)
  )

  val db = Database.forDataSource(DB.getDataSource())

  def flatten3[A, B, C](t: ((A, B), C)) : (A, B, C) = (t._1._1, t._1._2, t._2)
  def flatten4[A, B, C, D](t: (((A, B), C), D)) : (A, B, C, D) = (t._1._1._1, t._1._1._2, t._1._2, t._2)
  def flatten5[A, B, C, D, E](t: ((((A, B), C), D), E)) : (A, B, C, D, E) = (t._1._1._1._1, t._1._1._1._2, t._1._1._2, t._1._2, t._2)

  def test = {
    Database.forDataSource(DB.getDataSource()) withTransaction {
      val tableList = MTable.getTables(None, Some("public"), None, None).list.map(t => (t.name.name, t.name.schema))
    }
  }
}