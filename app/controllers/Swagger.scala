package controllers

import java.io.File

import com.josephpconley.swagger2postman.models.swagger.v12
import com.josephpconley.swagger2postman.{CollectionArgs, CollectionFormats}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller}

import scala.concurrent.{Future, Await}
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

  def generate = Action.async(parse.json) { implicit req =>
    Json.fromJson[GenerateRequest](req.body).fold(
      errors => Future.successful {
        Logger.error(JsError.toFlatJson(errors).toString())
        BadRequest
      },
      gen => gen.selectedVersion match {
        case 1.2 => {
          val args = CollectionArgs(gen.host, gen.name, gen.headers)
          WS.url(args.docUrl).get().map { res =>
            val doc = Json.fromJson[v12.SwaggerDoc](res.json).get
            Ok(Swagger2PostmanV12.toPostman(doc, args))
          }
        }
        case 2.0 => {

          Ok(Swagger2PostmanV12.toPostman(doc, args))
        }
      }
    )
  }
}

case class GenerateRequest(host: String, name: String, selectedVersion: Int, swaggerJson: String, headers: Map[String, String] = Map.empty)

object Swagger2PostmanV12 extends v12.Swagger2Postman {

  def execute(url: String) = Await.result(WS.url(url).get(), Duration.Inf).body
}