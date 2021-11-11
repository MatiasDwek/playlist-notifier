package com.playlistnotifier

import akka.actor._
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.util.Timeout
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.playlistnotifier.PlaylistHandler.{PlaylistRequest, name}

object PlaylistHandler {
  def apply(): Behavior[PlaylistRequest] = Behaviors.setup(context => new PlaylistHandler(context))

  def name = "playlistHandler"

  // TODO extract into a class of its own
  case class Playlist(name: String)

  sealed trait PlaylistRequest

  final case class GetPlaylist(name: String, replyTo: ActorRef[PlaylistResponse]) extends PlaylistRequest

  final case class GetPlaylists(replyTo: ActorRef[PlaylistResponse]) extends PlaylistRequest

  final case class FollowPlaylist(name: String, replyTo: ActorRef[PlaylistResponse]) extends PlaylistRequest

  final case class GetFollowedPlaylists(replyTo: ActorRef[PlaylistResponse]) extends PlaylistRequest

  sealed trait PlaylistResponse

  case class PlaylistFound(playlist: Playlist) extends PlaylistResponse

  case class PlaylistsFound(playlists: Vector[Playlist]) extends PlaylistResponse

  case class PlaylistFollowed(playlist: Playlist) extends PlaylistResponse

  case object PlaylistAlreadyFollowed extends PlaylistResponse

  case object PlaylistNotFound extends PlaylistResponse
}

class PlaylistHandler(context: ActorContext[PlaylistRequest]) extends AbstractBehavior[PlaylistRequest](context) {

  import PlaylistHandler._

  var followedPlaylists = Vector.empty[Playlist]

  override def onMessage(msg: PlaylistRequest): Behavior[PlaylistRequest] = msg match {
    case GetPlaylist(name, replyTo) =>
      context.log.info(s"Received request get playlist $name")
      replyTo ! PlaylistFound(Playlist(name))
      this
    case GetPlaylists(replyTo) =>
      context.log.info("Received request get all playlists")
      replyTo ! PlaylistsFound(Vector(Playlist("Playlist placeholder")))
      this
    case FollowPlaylist(name, replyTo) =>
      context.log.info(s"Received request follow playlist $name")
      val playlistRequest = Playlist(name)
      if (followedPlaylists.contains(playlistRequest)) {
        replyTo ! PlaylistAlreadyFollowed
      } else {
        followedPlaylists = followedPlaylists :+ Playlist(name)
        replyTo ! PlaylistFollowed(Playlist(name))
      }
      this
    case GetFollowedPlaylists(replyTo) =>
      context.log.info("Received request get all followed playlists")
      replyTo ! PlaylistsFound(followedPlaylists)
      this
  }


}
