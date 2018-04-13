package com.soujiro0725.twitter

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl._
import akka.stream.scaladsl.{Sink, Source}
import akka.testkit.TestKit
import com.amazonaws.services.dynamodbv2.model._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class DynamodbSpec extends TestKit(ActorSystem("DynamodbSpec"))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  val settings =DynamoSettings(system)

  override def beforeAll() = {
    // TODO make it read from ENV variables
    System.setProperty("awsaccessKeyId", "some")
    System.setProperty("aws.secretKey", "some")
  }

  "DynamoDB client" should {

    "provide a simpel usage" in {
      implicit val system = ActorSystem()
      implicit val materializer = ActorMaterializer()

      val settings = DynamoSettings(system)
      val client = DynamoClient(settings)

      import DynamoImplicits._
      val listTablesResult: Future[ListTablesResult] = client.single(new ListTablesRequest())

      Await.result(listTablesResult, 5.seconds)
      system.terminate()
    }
  }
}
