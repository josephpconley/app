package models.api

//import scala.slick
//import slick.driver.PostgresDriver.simple._
//import Database.threadLocalSession
//import play.api.Logger
//import play.api.db.DB
//import play.api.Play.current
//import scala.slick.lifted.ColumnOption.DBType
//
//import scala.slick.jdbc.{GetResult, StaticQuery => Q}
//import scala.slick.lifted.ColumnOption.DBType
//import org.joda.time.DateTime
//import play.api.libs.oauth.{RequestToken, ServiceInfo, OAuth, ConsumerKey}
//import models.Models._

/**
 * User: joe
 * Date: 7/24/13
 *
 *
 * Quickbooks
 * val consumerKey = "qyprdklNiBy4NXiWN5f4PTN0d6JcPN"
  val consumerSecret = "boNCRx20ZbIeslEIxT2EOm9VI3L1FMANyArr6jp5"
  val KEY = ConsumerKey(consumerKey, consumerSecret)

 val requestTokenURL = "https://oauth.intuit.com/oauth/v1/get_request_token"
  val accessTokenURL = "https://oauth.intuit.com/oauth/v1/get_access_token"
  val authorizationURL = "https://appcenter.intuit.com/Connect/Begin"


 */

/*

case class OAuthApp(consumerKey: String, consumerSecret: String, requestTokenURL: String, accessTokenURL: String, authorizationURL: String, id: Option[Long] = None){
  def KEY = ConsumerKey(consumerKey, consumerSecret)
  def auth = OAuth(ServiceInfo(requestTokenURL, accessTokenURL, authorizationURL, KEY), false)
}

object OAuthAppTable extends Table[OAuthApp]("oauth_app"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def consumerKey = column[String]("consumer_key", DBType("varchar(50)"))
  def consumerSecret = column[String]("consumer_secret", DBType("varchar(50)"))
  def requestTokenURL = column[String]("request_token_url", DBType("text"))
  def accessTokenURL = column[String]("access_token_url", DBType("text"))
  def authorizationURL = column[String]("authorization_url", DBType("text"))
  def * = consumerKey ~ consumerSecret ~ requestTokenURL ~ accessTokenURL ~ authorizationURL ~ id.? <> (OAuthApp, OAuthApp.unapply _)

  def findById(id: Long) : Option[OAuthApp] = {
    Database.forDataSource(DB.getDataSource()) withTransaction {
      Query(OAuthAppTable).where(_.id === id).firstOption
    }
  }
}

case class OToken(appId: Long, token:String, secret:String, realmId:String, lastUpdated: DateTime = new DateTime){
  def reqToken = RequestToken(token, secret)
}

object OTokenTable extends Table[OToken]("oauth_token"){
  def appId = column[Long]("app_id", O.PrimaryKey)
  def token = column[String]("token", DBType("text"))
  def secret = column[String]("secret", DBType("text"))
  def realmId = column[String]("realm_id", DBType("varchar(50)"))
  def lastUpdated = column[DateTime]("last_updated")
  def * = appId ~ token ~ secret ~ realmId ~ lastUpdated <> (OToken, OToken.unapply _)
  def app = foreignKey("app_fk", appId, OAuthAppTable)(_.id)

  def create(token: OToken) = {
    Database.forDataSource(DB.getDataSource()) withTransaction {
      OTokenTable.insert(token)
    }
  }

  def findByAppId(id: Long) : Option[OToken] = {
    Database.forDataSource(DB.getDataSource()) withTransaction {
      Query(OTokenTable).where(_.appId === id).firstOption
    }
  }
}
*/