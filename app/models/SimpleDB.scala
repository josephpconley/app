package models

import java.sql.{DriverManager, Connection, Statement, PreparedStatement, ResultSet}
import javax.sql.DataSource
import scala.collection.mutable.ListBuffer

/**
 * User: jconley
 * Date: 11/1/13
 *
 * Copy of DB.scala from scala-simple-jdbc (renamed to SimpleDB)
 *
 * Source: https://code.google.com/p/scala-simple-jdbc/source/browse/src/main/scala/DB.scala
 */

trait SimpleDB {
  def getConnection: Connection

  def use[T](autoCommit: Boolean = true)(action: Connection => T): T = {
    val conn = getConnection
    try {
      conn.setAutoCommit(autoCommit)
      val ret = action(conn)
      if (!autoCommit) conn.commit()
      ret
    } catch {
      case e : Throwable =>
        if (!autoCommit) conn.rollback()
        throw e
    } finally {
      close(conn)
    }
  }

  def update(conn: Connection, sql: String, params: Seq[Seq[_]] = Nil, timeoutSec: Int = 0): Seq[Int] = {
    val st = conn.prepareStatement(sql)
    try {
      st.setQueryTimeout(timeoutSec)
      if (supportBatch(conn)) {
        if (params.isEmpty) st.addBatch()
        else for (p <- params) { setParams(st, p); st.addBatch() }
        st.executeBatch().toSeq
      } else {
        val updates = ListBuffer[Int]()
        if (params.isEmpty) updates += st.executeUpdate()
        for (p <- params) { setParams(st, p); updates += st.executeUpdate() }
        updates.toSeq
      }
    } finally {
      close(st)
    }
  }

  def execute(conn: Connection, sql: String) {
    val st = conn.prepareStatement(sql)
    try { st.execute() } finally { close(st) }
  }

  def query[T](conn: Connection, sql: String, params: Seq[_] = Nil, timeoutSec: Int = 0)(mapper: ResultSet => T): Seq[T] = {
    val st = conn.prepareStatement(sql)
    try {
      setParams(st, params)
      val result = ListBuffer[T]()
      val rs = st.executeQuery
      try {
        while (rs.next) result += mapper(rs)
      } finally {
        close(rs)
      }
      result.toSeq
    }
  }

  private def supportBatch(conn: Connection) = conn.getMetaData.supportsBatchUpdates

  private def setParams(st: PreparedStatement, params: Seq[_]) {
    for (i <- 1 to params.size) { st.setObject(i, params(i - 1)) }
  }

  private def close(resource: Any) {
    resource match {
      case c: Connection => c.close()
      case s: Statement => s.close()
      case r: ResultSet => r.close()
      case _ => // do nothing
    }
  }
}

object SimpleDB {
  def apply(driver: String, url: String, user: String, pass: String) = new SimpleDB {
    override def getConnection = {
      Class.forName(driver)
      DriverManager.getConnection(url, user, pass)
    }
  }
  def apply(ds: DataSource) = new SimpleDB {
    override def getConnection = ds.getConnection
  }
}