package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object SecretFetcher {
  def props(implicit timeout: Timeout): Props = Props(new StreamingServiceProxy)

  def name = "secretFetcher"

  case class Placeholder(name: String)
}

class SecretFetcher(implicit timeout: Timeout) extends Actor with ActorLogging {

  import SecretFetcher._

  def receive: Receive = {
    case Placeholder(name) =>
      log.info(s"info")
  }
}
