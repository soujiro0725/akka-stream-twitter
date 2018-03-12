package com.soujiro0725.twitter

trait TwitterApp {

  this: TwitterService

  val app: TwitterStream

  class TwitterStream {
    def execute(): Unit = {
      println("running twitter app ...")

      // config.mode match
      // TODO
      twitterAPI.streamByTrack(Seq("nba", "nytimes"), new StatusListenerImpl())
    }
  }
}
