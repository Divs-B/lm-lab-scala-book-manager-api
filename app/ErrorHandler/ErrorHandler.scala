package ErrorHandler

import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.Results.Status
import play.api.mvc.{RequestHeader, Result}
import javax.inject.Singleton
import scala.concurrent.Future
import play.api.http.Status.{BAD_REQUEST, NOT_FOUND}
import play.api.libs.json._
import play.api.mvc.Results._

@Singleton
class ErrorHandler extends DefaultHttpErrorHandler {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case NOT_FOUND => Future.successful(
        Status(statusCode)(Json.obj("status" -> "Error!", "message" -> "Book not Found!"))
      )
      case BAD_REQUEST => Future.successful(
        Status(statusCode)(Json.obj("status" -> "Error!", "message" -> "Bad request!"))
      )
      case _ => Future.successful(
        Status(statusCode)(Json.obj("status" -> "Error!", "message" -> message))
      )
    }
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful(
      InternalServerError(Json.obj("status" -> "Server error!", "message" -> exception.getMessage))
    )
  }
}
