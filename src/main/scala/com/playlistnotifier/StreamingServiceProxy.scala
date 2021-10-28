package com.playlistnotifier

import akka.actor._
import akka.util.Timeout

object StreamingServiceProxy {
  def props(implicit timeout: Timeout): Props = Props(new StreamingServiceProxy)

  def name = "streamingServiceProxy"

  case class Placeholder(name: String)
}

class StreamingServiceProxy(implicit timeout: Timeout) extends Actor with ActorLogging {

  import StreamingServiceProxy._

  def receive: Receive = {
    case Placeholder(name) =>
      log.info(s"info")
  }
}
