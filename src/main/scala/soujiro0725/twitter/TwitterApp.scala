package com.soujiro0725.twitter

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Await
import scala.concurrent.duration._

trait TwitterApp {

  this: TwitterService =>

  val app: TwitterStream

  class TwitterStream {

    implicit val actorSystem = ActorSystem("twitter-app")
    implicit val materializer = ActorMaterializer()
    private[this] val logger = Logger[TwitterStream]

    def execute(): Unit = {
      println("running twitter app ...")

      val source = Source.queue[Status](8, OverflowStrategy.backpressure)

      twitterAPI.streamByTrack(Seq("nba", "nytimes"), new StatusListenerImpl())
    }
  }
}