package com.playlistnotifier

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object PlaylistNotifier {
  def apply(): Behavior[Nothing] =
    Behaviors.setup[Nothing] { context =>
      context.log.info("Playlist Handler started")
      context.spawn(PlaylistHandler(), "playlist-handler")

      Behaviors.empty
    }
}
