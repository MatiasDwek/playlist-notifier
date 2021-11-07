package com.playlistnotifier

import akka.actor._
import akka.util.Timeout
import com.playlistnotifier.SecretFetcher.GetAccessToken


object StreamingServiceProxy {
  def props(sysEnv: Map[String, String])(implicit timeout: Timeout): Props = Props(new StreamingServiceProxy(sysEnv))

  def name = "streamingServiceProxy"

  case object GetStreamingServicePlaylists
}

class StreamingServiceProxy(sysEnv: Map[String, String])(implicit timeout: Timeout) extends Actor with ActorLogging {

  import StreamingServiceProxy._
  import akka.pattern.{ask, pipe}

  val secretFetcher: ActorRef = context.actorOf(SecretFetcher.props(sysEnv), SecretFetcher.name)

  def receive: Receive = {
    case GetStreamingServicePlaylists =>
      log.info(s"Received get streaming service playlist $name")
      val accessToken = secretFetcher.ask(GetAccessToken).mapTo[Option[String]]
    //      accessToken.pipeToSelf(sender())
  }
}
