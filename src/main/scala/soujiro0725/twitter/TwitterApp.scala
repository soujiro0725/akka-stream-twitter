package com.soujiro0725.twitter

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import com.typesafe.scalalogging.Logger
import scala.concurrent.Await
import scala.concurrent.duration._
import twitter4j.{User, Status}
import com.soujiro0725.twitter.SentimentAnalysisUtils._

case class Tweet(statusId: Long, user: User, uris: Iterable[String], datetime: String, rt: Int)
case class TweetSentiment(sentimentValue: Double)
case class TweetTuple(tweet: Tweet, sentiment: TweetSentiment)

trait TwitterApp {

  this: TwitterService =>

  val app: TwitterStream

  class TwitterStream {

    implicit val actorSystem = ActorSystem("twitter-app")
    implicit val materializer = ActorMaterializer()

    def execute(): Unit = {
      println("running twitter app ...")

      val source = Source.queue[Status](8, OverflowStrategy.backpressure)
      val flows = new Flows
      val sinks = new Sinks

      val streamQueue = source
        .via(flows.analyzeFlow)
        .to(sinks.dbSink)
        .run()

      def onStatus(status: Status): Unit = {
        streamQueue.offer(status)
      }

      twitterAPI.streamByTrack(Seq("btc", "virtual currency", "bitcoin"), new StatusListenerImpl(Some(onStatus)))

      Await.result(actorSystem.whenTerminated, Duration.Inf)
    }
  }

  class Flows {

    // val func1 = Flow.fromFunction((s: Status) => {
    //   val uris = s.getExtendedMediaEntities.map { me => me.getMediaURLHttps }.toSeq
    //   Tweet(s.getId, s.getUser, uris)
    // })

    val analyzeFlow = Flow.fromFunction((s: Status) => {
      val st = detectSentiment(s.getText)
      val uris = s.getExtendedMediaEntities.map { me => me.getMediaURLHttps }.toSeq
      val datetime = s.getCreatedAt.getTime().toString
      TweetTuple(Tweet(s.getId, s.getUser, uris, datetime, s.getRetweetCount), TweetSentiment(st))
    })
  }

  class Sinks {
    private[this] val logger = Logger[TwitterStream]

    val sink1 = Sink.foreach[Tweet] { mt =>
      logger.info(s"https://twitter.com/${mt.user.getScreenName}/status/${mt.statusId}, ${mt.uris}")
    }

    val dbSink = Sink.foreach[TweetTuple] { obj =>
      val tableName = "twitter-sentiment3"
      val result = DBClient.createTable(tableName)
      println("result of creating table is $result")
      DBClient.put(tableName, obj)
      //logger.info(s"${obj.sentimentValue.toString}")
    }

  }
}
