package com.playlistnotifier

import akka.actor._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class RestApi(system: ActorSystem, timeout: Timeout)
  extends RestRoutes {
  implicit val requestTimeout: Timeout = timeout

  implicit def executionContext: ExecutionContextExecutor = system.dispatcher

  def createPlaylistHandler(): ActorRef = system.actorOf(PlaylistHandler.props, PlaylistHandler.name)
}

trait RestRoutes extends PlaylistHandlerApi with PlaylistMarshalling {

  import StatusCodes._

  def routes: Route = playlistRoute

  def playlistRoute: Route =
    pathPrefix("playlists" / Segment) { playlist =>
      pathEndOrSingleSlash {
        get {
          // GET /playlists/:playlist
          onSuccess(getPlaylist(playlist)) {
            _.fold(complete(NotFound))(p => complete(OK, p))
          }
        } ~
          post {
            // POST /playlists/:playlist
            onSuccess(followPlaylist(playlist)) {
              case PlaylistHandler.PlaylistNotFound => val err = Error(s"$playlist playlist not found.")
                complete(NotFound, err)
              case PlaylistHandler.PlaylistFollowed(playlist) =>
                complete(Created, playlist)
              case PlaylistHandler.PlaylistAlreadyFollowed =>
                val err = Error(s"$playlist already followed.")
                complete(BadRequest, err)
            }
          }
      }
    }

}

trait PlaylistHandlerApi {

  import PlaylistHandler._

  def createPlaylistHandler(): ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  lazy val playlistHandler: ActorRef = createPlaylistHandler()

  def getPlaylist(playlist: String): Future[Option[Playlist]] = {
    playlistHandler.ask(GetPlaylist(playlist)).mapTo[Option[Playlist]]
  }

  def getPlaylists: Future[PlaylistResponse] = {
    playlistHandler.ask(GetPlaylists).mapTo[PlaylistResponse]
  }

  def followPlaylist(playlist: String): Future[PlaylistResponse] = {
    playlistHandler.ask(FollowPlaylist(playlist)).mapTo[PlaylistResponse]
  }
}
