package controllers

import java.io.File

import com.josephpconley.swagger2postman.Swagger2Postman
import com.josephpconley.swagger2postman.models.{CollectionFormats, CollectionArgs}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

import scala.io.Source

object Swagger
  extends Controller
  with CollectionFormats {

  def index = Action { implicit req =>
    Ok(views.html.swagger.swagger2postman())
  }

  def ui = Action { implicit req =>
    Ok(views.html.swagger.swaggerUI())
  }

  def spec(version: String) = Action { implicit req =>
    Ok(Source.fromFile(new File("petstore12.json")).mkString)
  }

  def generate = Action(parse.json) { implicit req =>
    Json.fromJson[CollectionArgs](req.body).fold(
      errors => {
        Logger.error(JsError.toFlatJson(errors).toString())
        BadRequest
      },
      args => Ok(Swagger2PostmanPlay.generate(args))
    )
  }
}

object Swagger2PostmanPlay extends Swagger2Postman {

  def execute(url: String) = Await.result(WS.url(url).get(), Duration.Inf).body
}