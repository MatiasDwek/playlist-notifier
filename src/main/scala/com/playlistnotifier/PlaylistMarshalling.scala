package com.playlistnotifier

import spray.json._

case class Error(message: String)

trait PlaylistMarshalling extends DefaultJsonProtocol {

  import PlaylistHandler._

  implicit val playlistFormat: RootJsonFormat[Playlist] = jsonFormat1(Playlist)
  implicit val playlistsFormat: RootJsonFormat[Playlist] = jsonFormat1(Playlist)
  implicit val errorFormat: RootJsonFormat[Error] = jsonFormat1(Error)
}
