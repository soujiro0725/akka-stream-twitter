package com.soujiro0725.twitter

import com.typesafe.scalalogging.Logger
import twitter4j._

trait TwitterService {
  val twitterService: TwitterService

  class Twitter {
    private[this] val logger = Logger[TwitterService]

    def streamByTrack():Unit = {

    }

    def streamByList():Unit = {

    }

    def getUserListMembers():Seq[User] = {

    }

    def getStatus():Status = {

    }

    def getFavorites[U]():Unit = {

    }

    
  }
}
