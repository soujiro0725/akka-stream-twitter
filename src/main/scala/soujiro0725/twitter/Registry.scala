package com.soujiro0725.twitter

import com.soujiro0725.twitter.{TwitterService, TwitterApp}

object Registry extends TwitterService with TwitterApp {
  override val twitterAPI = new TwitterAPI
  override val app = new TwitterStream
}
