package com.soujiro0725.twitter

import com.typesafe.scalalogging.Logger
import twitter4j._

trait TwitterService {
  val twitterAPI: TwitterAPI

  /**
    * to hide implementation, define API as an inner class
    */
  class TwitterAPI {
    private[this] val logger = Logger[TwitterService]

    def streamByTrack(tracks: Seq[String], listener: StatusListener): Unit = {
      val twitterStream = new TwitterStreamFactory().getInstance()
      twitterStream.addListener(listener)

      val query = new FilterQuery()
        .track(tracks: _*)
      twitterStream.filter(query)
    }
  }
}
