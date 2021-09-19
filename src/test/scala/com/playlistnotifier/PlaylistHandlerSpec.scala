package com.playlistnotifier

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.playlistnotifier.PlaylistHandler.{FollowPlaylist, GetPlaylist, Playlist, PlaylistFollowed, PlaylistFound}
import org.scalatest.{MustMatchers, WordSpecLike}

class PlaylistHandlerSpec extends TestKit(ActorSystem("testPlaylistHandler"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {
  "The PlaylistHandler" must {

    "Get a playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      val playlistName = "my jams"
      playlistHandler ! GetPlaylist(playlistName)
      expectMsg(PlaylistFound(Playlist(playlistName)))
    }

    "Follow a playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      val playlistName = "my jams"
      playlistHandler ! FollowPlaylist(playlistName)
      expectMsg(PlaylistFollowed(Playlist(playlistName)))
    }
  }


}
