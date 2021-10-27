package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object PlaylistHandler {
  def props(implicit timeout: Timeout): Props = Props(new PlaylistHandler)

  def name = "playlistHandler"

  case class GetPlaylist(name: String)

  case object GetPlaylists

  case class FollowPlaylist(name: String)

  case class Playlist(name: String)

  case class Playlists(playlists: Vector[Playlist])

  sealed trait PlaylistResponse

  case class PlaylistFollowed(playlist: Playlist) extends PlaylistResponse

  case object PlaylistAlreadyFollowed extends PlaylistResponse

  case object PlaylistNotFound extends PlaylistResponse
}

class PlaylistHandler(implicit timeout: Timeout) extends Actor with ActorLogging {

  import PlaylistHandler._

  var followedPlaylists = Vector.empty[Playlist]

  def receive: Receive = {
    case GetPlaylist(name) =>
      log.info(s"Received request get playlist $name")
      sender() ! Playlist(name)
    case GetPlaylists =>
      log.info(s"Received request get all playlists")
      sender() ! Playlists(Vector(Playlist("Playlist placeholder")))
    case FollowPlaylist(name) =>
      log.info(s"Received request follow playlist $name")
      val playlistRequest = Playlist(name)
      if (followedPlaylists.contains(playlistRequest)) {
        sender() ! PlaylistAlreadyFollowed
      } else {
        followedPlaylists = followedPlaylists :+ Playlist(name)
        sender() ! PlaylistFollowed(Playlist(name))
      }
  }
}
