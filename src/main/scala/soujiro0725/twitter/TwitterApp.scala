package com.soujiro0725.twitter

trait TwitterApp {

  val app: TwitterStream

  class TwitterStream {
    def execute(): Unit = {
      println("running twitter app ...")
    }
  }
}
