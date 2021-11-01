package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object SecretFetcher {
  def props(sysEnv: Map[String, String])(implicit timeout: Timeout): Props = Props(new SecretFetcher(sysEnv))

  def name = "secretFetcher"

  case object GetAccessToken
}

class SecretFetcher(sysEnv: Map[String, String])(implicit timeout: Timeout) extends Actor with ActorLogging {

  import SecretFetcher._

  def receive: Receive = {
    case GetAccessToken =>
      log.info(s"Get access token for current user")
      sender() ! sysEnv("access_token")
  }
}
