package models

import reactivemongo.bson.BSONObjectID
import play.api.libs.json.Json

/**
 * @param _id the mongo db id, we provide a string one for simplicity
 * @param title a mandatory title
 * @param description an optional description
 */
case class Movie(
                 _id : String = BSONObjectID.generate.toString(),
                 title : String,
                 description : Option[String] = None
                 )

/**
 * Companion object provides JSON serialization
 */
object Movie {
  implicit val moviesFormat = Json.format[Movie]
}