package com.playlistnotifier

import com.wrapper.spotify.SpotifyApi

trait StreamingService {
  def getPlaylists(accessToken: String): AnyRef
}

class Spotify extends StreamingService {
  override def getPlaylists(accessToken: String): AnyRef = {
    val spotifyApi: SpotifyApi = new SpotifyApi.Builder().setAccessToken(accessToken).build
    spotifyApi.getListOfCurrentUsersPlaylists.build

  }


}