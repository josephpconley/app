package controllers

import play.api.mvc.{RequestHeader, Action, Controller}
import play.api.libs.oauth._
import play.api.libs.ws.WS
import play.api.libs.oauth.ServiceInfo
import play.api.libs.oauth.OAuth
import play.api.libs.oauth.RequestToken
import play.api.libs.oauth.ConsumerKey

import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * User: jconley
 * Date: 12/6/13
 */
object Twitter extends Controller {

  val KEY = ConsumerKey("7uAQfWwEyIuGwqjyYkEzRQ", "sbmVHgNBnQtJ8QjUKrrljPJWDkFbHXlxGLGkIRrV5Q")

  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY),
    false)

  def index = Action.async { request =>
    Twitter.sessionTokenPair(request).map{ tokens =>
      WS.url("https://api.twitter.com/1.1/statuses/home_timeline.json").sign(OAuthCalculator(Twitter.KEY, tokens)).get.map{r =>
        Ok(r.body)
      }
    }.getOrElse(Future(Redirect(routes.Twitter.authenticate())))
  }

  def authenticate = Action { request =>
    request.queryString.get("oauth_verifier").flatMap(_.headOption).map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the authorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Twitter.index).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TWITTER.retrieveRequestToken("http://localhost:9000/twitter/auth") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    )
  }

  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }
}