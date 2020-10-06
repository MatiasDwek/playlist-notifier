package com.playlistnotifier

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.playlistnotifier.PlaylistHandler.{FollowPlaylist, Playlist, PlaylistFollowed}
import org.scalatest.{MustMatchers, WordSpecLike}

class PlaylistHandlerSpec extends TestKit(ActorSystem("testPlaylistHandler"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {
  "The PlaylistHandler" must {

    "Follow a playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      val playlistName = "my jams"
      playlistHandler ! FollowPlaylist(playlistName)
      expectMsg(PlaylistFollowed(Playlist(playlistName)))
    }
  }


}
