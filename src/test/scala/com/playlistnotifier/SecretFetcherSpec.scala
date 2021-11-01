package com.playlistnotifier

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.playlistnotifier.SecretFetcher.GetAccessToken
import org.scalatest.{MustMatchers, WordSpecLike}

class SecretFetcherSpec extends TestKit(ActorSystem("testSecretFetcher"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with StopSystemAfterAll {
  val PlaylistName = "my jams"

  "The SecretFetcher" must {
    "Get a secret for a user" in {
      val accessToken = "some-access-token-123"
      val sysEnv = Map("access_token" -> accessToken)
      val secretFetcher = system.actorOf(SecretFetcher.props(sysEnv))
      secretFetcher ! GetAccessToken
      expectMsg(accessToken)
    }
  }
}
