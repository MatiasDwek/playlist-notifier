package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object SecretFetcher {
  def props(implicit timeout: Timeout): Props = Props(new StreamingServiceProxy)

  def name = "secretFetcher"

  case class GetAccessToken(user: String)
}

class SecretFetcher(implicit timeout: Timeout) extends Actor with ActorLogging {

  import SecretFetcher._

  def receive: Receive = {
    case GetAccessToken(user) =>
      log.info(s"Get access token for user $user")
      val accessToken = sys.env("access_token") // FIXME
      sender() ! accessToken
  }
}
