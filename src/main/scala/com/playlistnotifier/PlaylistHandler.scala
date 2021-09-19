package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object PlaylistHandler {
  def props(implicit timeout: Timeout): Props = Props(new PlaylistHandler)

  def name = "playlistHandler"

  case class GetPlaylist(name: String)

  case class FollowPlaylist(name: String)

  case class Playlist(name: String)

  sealed trait PlaylistResponse

  case class PlaylistFollowed(playlist: Playlist) extends PlaylistResponse

  case object PlaylistAlreadyFollowed extends PlaylistResponse

  case class PlaylistFound(playlist: Playlist) extends PlaylistResponse

  case object PlaylistNotFound extends PlaylistResponse
}

class PlaylistHandler(implicit timeout: Timeout) extends Actor with ActorLogging {

  import PlaylistHandler._

  def receive: Receive = {
    case GetPlaylist(name) => {
      log.info(s"Received playlist request $name")
      sender() ! PlaylistFound(Playlist(name))
    }
    case FollowPlaylist(name) => {
      log.info(s"Received playlist $name")
      sender() ! PlaylistFollowed(Playlist(name))
    }

  }
}
