package com.soujiro0725.twitter

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl._
import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.{ListTablesResult, ListTablesRequest}
import scala.concurrent.Future

/**
  * export AWS_CREDENTIAL_PROFILES_FILE=~/.aws/credentials
  */
object DBClient {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val settings = DynamoSettings(system)
  val client = DynamoClient(settings)

  val listTableResult: Future[ListTablesResult] = client.single(new ListTablesRequest())

  def update() {

  }

}
