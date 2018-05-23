package controllers

import models.Movie
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import utils.SecureActions


/**
 * REST API Controller providing CRUD operations for Movies with Reactive Mongo in a full async API
 */
object Application extends Controller with MongoController with SecureActions {

  /**
   * A reference of a JSON style collection in Mongo
   */
  private def moviesCollection = db.collection[JSONCollection]("movies")

  /**
   * Convinience helper thar marshalls json or sends a 404 if none found
   */
  private def asJson(v: Option[JsObject]) = v.map(Ok(_)).getOrElse(NotFound)

  /**
   * Default index entry point
   */
  def index = Action {
    Ok(views.html.index("cinema-scala"))
  }

  /**
   * Actions that reactively list all movies in the collection
   */
  def listMovies() = SimpleAuthenticatedAction {
    _ =>
      moviesCollection
        .find(Json.obj())
        .cursor[JsObject]
        .collect[List]() map {
        movies =>
          Ok(JsArray(movies))
      }
  }

  /**
   * Finds a movie by Id
   */
  def findMovie(id: String) = SimpleAuthenticatedAction {
    _ =>
      moviesCollection
        .find(Json.obj("_id" -> id))
        .one[JsObject] map asJson
  }

  /**
   * Adds a movie
   */
  def addMovie() = JsonAuthenticatedAction[Movie] {
    (movie, _) =>
      moviesCollection.insert(movie) map {
        _ => Ok(Json.toJson(movie))
      }
  }

  /**
   * Partially updates the properties of a movie
   */
  def updateMovie(id: String) = JsonAuthenticatedAction[JsObject] {
    (json, _) =>
      for {
        _ <- moviesCollection.update(Json.obj("_id" -> id), Json.obj("$set" -> json))
        newMovie <- moviesCollection.find(Json.obj("_id" -> id)).one[JsObject]
      } yield asJson(newMovie)
  }

  /**
   * Deletes a movie by id
   */
  def deleteMovie(id: String) = SimpleAuthenticatedAction {
    _ =>
      for {
        newMovie <- moviesCollection.find(Json.obj("_id" -> id)).one[JsObject]
        _ <- moviesCollection.remove(Json.obj("_id" -> id))
      } yield asJson(newMovie)
  }

}