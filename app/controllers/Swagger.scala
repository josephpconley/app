package controllers

import java.io.{File, PrintWriter}

import com.josephpconley.swagger2postman.models.swagger.v12
import com.josephpconley.swagger2postman.models.swagger.v2
import com.josephpconley.swagger2postman.{CollectionArgs, CollectionFormats}
import org.scalactic.{Bad, Good, One, Or}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

import scala.io.Source
import scala.util.{Failure, Success, Try}

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

  implicit val fmt = Json.format[GenerateRequest]

  def generate = Action.async(parse.json) { implicit req =>
    Json.fromJson[GenerateRequest](req.body).fold(
      errors => Future.successful {
        Logger.error(JsError.toFlatJson(errors).toString())
        BadRequest
      },
      gen => convert(gen).map {
        case Good(res) => Ok(res)
        case Bad(err) => BadRequest(err)
      }
    )
  }

  def download = Action.async(parse.json) { implicit req =>
    Json.fromJson[GenerateRequest](req.body).fold(
      errors => Future.successful {
        Logger.error(JsError.toFlatJson(errors).toString())
        BadRequest
      },
      gen => convert(gen).map {
        case Good(res) =>
          val filename = gen.name + ".json"
          new PrintWriter(filename) {
            write(res.toString())
            close
          }
          Ok.sendFile(new File(filename))
        case Bad(err) => BadRequest(err)
      }
    )
  }

  private def convert(req: GenerateRequest): Future[JsValue Or JsValue] = {
    req.selectedVersion match {
      case "1.2" => {
        val args = CollectionArgs(req.host.getOrElse(""), req.name, req.headers)
        WS.url(args.docUrl).get().map { res =>
          val doc = Json.fromJson[v12.SwaggerDoc](res.json).get
          Good(Swagger2PostmanV12.toPostman(doc, args))
        }
      }
      case "2.0" => Future.successful {
        Json.fromJson[v2.SwaggerDoc](req.swaggerJson).fold(
          invalid => {
            println("Error converting Swagger v2 doc to Postman json")
            println(JsError.toFlatJson(invalid).toString())
            Bad(JsError.toFlatJson(invalid))
          },
          swaggerDoc => {
            val args = CollectionArgs(req.host.getOrElse(""), req.name, req.headers)
            val postmanJson = Swagger2PostmanV2.toPostman(swaggerDoc, args)
            Good(postmanJson)
          }
        )
      }
      case _ => Future.successful(Bad(JsString(s"Invalid Swagger version number ${req.selectedVersion}")))
    }
  }
}

case class GenerateRequest(name: String, selectedVersion: String, swaggerJson: JsValue, host: Option[String] = None, headers: Map[String, String] = Map.empty)

object Swagger2PostmanV12 extends v12.Swagger2Postman {
  def execute(url: String) = Await.result(WS.url(url).get(), Duration.Inf).body
}

object Swagger2PostmanV2 extends v2.Swagger2Postman


//  def swagger20(name: String) = Action(parse.json) { implicit req =>
//    Json.fromJson[v2.SwaggerDoc](req.body).fold(
//      invalid => {
//        println("Error converting Swagger v2 doc to Postman json")
//        println(JsError.toFlatJson(invalid).toString())
//        BadRequest(JsError.toFlatJson(invalid))
//      },
//      swaggerDoc => {
//        val args = CollectionArgs("", name, req.queryString.collect { case (key, value) if key != "name" => key -> value.head})
//        val postmanJson = Swagger2PostmanV2.toPostman(swaggerDoc, args)
//        Ok(postmanJson)
//      }
//    )
//
//  }