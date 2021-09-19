package com.playlistnotifier

import akka.actor._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class RestApi(system: ActorSystem, timeout: Timeout)
  extends RestRoutes {
  implicit val requestTimeout: Timeout = timeout

  implicit def executionContext: ExecutionContextExecutor = system.dispatcher

  def createPlaylistHandler(): ActorRef = system.actorOf(PlaylistHandler.props, PlaylistHandler.name)
}

trait RestRoutes extends PlaylistHandlerApi
  with PlaylistMarshalling {

  import StatusCodes._

  def routes: Route = playlistRoute

  def playlistRoute: Route =
    pathPrefix("playlists" / Segment) { playlist =>
      pathEndOrSingleSlash {
        post {
          println(s"TRACE ---->  $playlist")
          // POST /playlists/:playlist
          onSuccess(followPlaylist(playlist)) {
            case PlaylistHandler.PlaylistFound(playlist) => complete(Created, playlist)
            case PlaylistHandler.PlaylistNotFound => val err = Error(s"$playlist playlist not found already.")
              complete(BadRequest, err)
            case PlaylistHandler.PlaylistFollowed(playlist) => complete(Created, playlist)
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

  def getPlaylist(playlist: String): Future[PlaylistResponse] = {
    playlistHandler.ask(GetPlaylist(playlist)).mapTo[PlaylistResponse]
  }

  def followPlaylist(playlist: String): Future[PlaylistResponse] = {
    playlistHandler.ask(FollowPlaylist(playlist)).mapTo[PlaylistResponse]
  }
}
