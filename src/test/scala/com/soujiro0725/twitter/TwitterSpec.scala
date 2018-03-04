package com.soujiro0725.twitter

import com.soujiro0725.twitter.TwitterService
import org.scalatest._

class TwitterServiceSpec extends
    FlatSpec with
    Matchers {

  "TwitterService" should "getUserList" in {
    val twitterService = new TwitterService()
    val list = twitterService.getUserListMembers("soujiro0725", "")
    list.nonEmpty should === (true)
  }
}
