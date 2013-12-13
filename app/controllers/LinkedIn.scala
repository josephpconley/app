package controllers

import play.api.mvc.{RequestHeader, Action, Controller}
import play.api.libs.ws.WS

import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * User: jconley
 * Date: 12/6/13
 */
object LinkedIn extends Controller {

  val API_KEY = "77t642l6zzlhpt"
  val SECRET_KEY = "RGsdJC8G9Qz2FHKf"
  val STATE = "BNADFASKFA1874012nbakjan95280"

  def AUTH_CODE_URL = "https://www.linkedin.com/uas/oauth2/authorization?response_type=code" +
                        s"&client_id=$API_KEY&state=$STATE&redirect_uri=$redirectURL"

  def ACCESS_TOKEN_URL(code:String) = "https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code" +
                                      s"&code=$code&redirect_uri=$redirectURL&client_id=$API_KEY&client_secret=$SECRET_KEY"

  val redirectURL = "http://localhost:9000/linkedin/auth"

  def index = Action.async { implicit request =>
    LinkedIn.sessionToken(request).map{ token =>
      WS.url("https://api.linkedin.com/v1/people/~?format=json&oauth2_access_token=" + token).get.map{r =>
        Ok(r.body)
      }
    }.getOrElse(Future(Redirect(routes.LinkedIn.authenticate())))
  }

  def authenticate = Action.async { implicit request =>
    request.queryString.get("code").flatMap(_.headOption).map { code =>
      //we have the auth code, let's get the access token
      WS.url(ACCESS_TOKEN_URL(code)).post("").map{r =>
        println(r.json)
        Redirect(routes.LinkedIn.index).withSession("token" -> (r.json \ "access_token").as[String], "code" -> code)
      }
    }.getOrElse(Future(Redirect(AUTH_CODE_URL)))
  }

  def sessionToken(implicit request: RequestHeader): Option[String] = request.session.get("token")
}