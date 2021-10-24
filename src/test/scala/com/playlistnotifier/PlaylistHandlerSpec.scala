package com.playlistnotifier

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.playlistnotifier.PlaylistHandler.{FollowPlaylist, GetPlaylist, Playlist, PlaylistAlreadyFollowed, PlaylistFollowed}
import org.scalatest.{MustMatchers, WordSpecLike}

class PlaylistHandlerSpec extends TestKit(ActorSystem("testPlaylistHandler"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {
  val PlaylistName = "my jams"

  "The PlaylistHandler" must {
    "Get a playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      playlistHandler ! GetPlaylist(PlaylistName)
      expectMsg(Playlist(PlaylistName))
    }

    "Follow a playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      playlistHandler ! FollowPlaylist(PlaylistName)
      expectMsg(PlaylistFollowed(Playlist(PlaylistName)))
    }

    "Return error when trying to follow an already followed playlist" in {
      val playlistHandler = system.actorOf(PlaylistHandler.props)
      playlistHandler ! FollowPlaylist(PlaylistName)
      expectMsg(PlaylistFollowed(Playlist(PlaylistName)))

      playlistHandler ! FollowPlaylist(PlaylistName)
      expectMsg(PlaylistAlreadyFollowed)
    }
  }


}
