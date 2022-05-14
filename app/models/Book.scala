package models

import play.api.libs.json.{Json, OFormat}

/**
 * We use a Scala case class to define our model object
 * This gives the added benefit of preventing duplicates because
 * in Scala case classes are said to be equal if all their member variables are equal
 * See more on here under the title "Comparison"
 * https://docs.scala-lang.org/tour/case-classes.html
 */
case class Book(id: Long, title: String, author: String, description: String, genre: String)

object Book {
  implicit val format: OFormat[Book] = Json.format[Book]
}