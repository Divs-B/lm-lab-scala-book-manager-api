package controllers

import models.Book
import play.api.mvc.{AbstractController, Action, AnyContent, BaseController, ControllerComponents}
import play.api.libs.json._
import repositories.BookRepository

import javax.inject.{Inject, Singleton}

@Singleton
class BooksController @Inject()(val cc: ControllerComponents, dataRepository: BookRepository) extends AbstractController(cc) {

  def getAll: Action[AnyContent] = Action {
    Ok(Json.toJson(dataRepository.getAllBooks))
  }

  def getBook(bookId: Long): Action[AnyContent] = Action {
    var bookToReturn: Book = null
    dataRepository.getBook(bookId) foreach { book =>
      bookToReturn = book
    }
    Ok(Json.toJson(bookToReturn))
  }

  def addBook(): Action[AnyContent] = Action {
    implicit request => {
      val requestBody = request.body
      val bookJsonObject = requestBody.asJson

      // This type of JSON un-marshalling will only work
      // if ALL fields are POSTed in the request body
      val bookItem: Option[Book] =
      bookJsonObject.flatMap(
        Json.fromJson[Book](_).asOpt
      )
      val savedBook: Option[Book] = dataRepository.addBook(bookItem.get)
      Created(Json.toJson(savedBook))
    }
  }


  def deleteBook(): Action[AnyContent] = Action {
    implicit request => {
      val postBody = request.body.asJson
      val bool = postBody.map { args =>
        val id: Long = args("id").asOpt[Long].get
        dataRepository.deleteBook(id)
      }
      Accepted(Json.toJson(bool))
      Redirect(routes.BooksController.getAll())
    }
  }


}
