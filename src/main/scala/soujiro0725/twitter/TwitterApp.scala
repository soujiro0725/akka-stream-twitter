package com.soujiro0725.twitter

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Await
import scala.concurrent.duration._

case class Tweet(statusId, Long, user: User, uris: Iterable[String])

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
      val flow = Flow.fromFunction((s: Status) => {
        val uris = s.getExtendedMediaEntities.map { me => me.getMediaURLHttps }.toSeq
        Tweet(s.getId, s.getUser, uris)
      })
      val sink = Sink.foreach[Tweet] { mt =>
        logger.info(s"https://twitter.com/${mt.user.getScreenName}/status/${mt.statusId}, ${mt.uris}")
      }

      val streamQueue = src.via(flows.doSomething).to(sink).run()

      twitterAPI.streamByTrack(Seq("nba", "nytimes"), new StatusListenerImpl())
    }
  }
}
