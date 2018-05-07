package com.soujiro0725.twitter

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl._
import akka.stream.alpakka.dynamodb.scaladsl.DynamoImplicits._
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model._
import scala.concurrent.Future
import scala.collection.JavaConverters._

/**
  * export AWS_CREDENTIAL_PROFILES_FILE=~/.aws/credentials
  * 
  * aws dynamodb create-table --table-name Persons \
  * --attribute-definitions AttributeName=Id,AttributeType=N \
  * --key-schema AttributeName=Id,KeyType=HASH \
  * --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
  * --endpoint-url http://localhost:32768
  * 
  * aws dynamodb list-tables --endpoint-url http://localhost:7777
  * 
  * aws dynamodb put-item --endpoint-url http://localhost:7777 --table-name twitter-sentiment3 --item '{"testId":"1"}'

  * aws dynamodb scan --table-name twitter-sentiment3 --endpoint-url http://localhost:7777
  */
object DBClient {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val settings = DynamoSettings(system)
  val client = DynamoClient(settings)

  val keyCol = "userId"
  val sortCol = "datetime" //unix time (String)

  def S(s: String) = new AttributeValue().withS(s)
  def N(n: Int) = new AttributeValue().withN(n.toString)

  def keyMap(hash: String, sort: Int): Map[String, AttributeValue] = Map(
    keyCol -> S(hash),
    sortCol -> N(sort)
  )

  def createTable(tableName: String) {

    val createTableRequest = new CreateTableRequest()
      .withTableName(tableName)
      .withKeySchema(
        new KeySchemaElement().withAttributeName(keyCol).withKeyType(KeyType.HASH),
        new KeySchemaElement().withAttributeName(sortCol).withKeyType(KeyType.RANGE)
      )
      .withAttributeDefinitions(
        new AttributeDefinition().withAttributeName(keyCol).withAttributeType("S"),
        new AttributeDefinition().withAttributeName(sortCol).withAttributeType("S")
      )
      .withProvisionedThroughput(
        new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(10L)
      )

    client.single(createTableRequest)
  }

  def listTable() {
    client.single(new ListTablesRequest())
  }

  /**
    * Map(
    *     
    * )
    * 
    * 
    * 
    */
  def put(tableName: String, tweetTuple: TweetTuple) {
    var batchWriteItemRequest = new BatchWriteItemRequest().withRequestItems(
      Map(
        tableName ->
          List(
            new WriteRequest(
              new PutRequest(
                Map(
                  "userId" -> new AttributeValue(tweetTuple.tweet.user.getScreenName),
                  "statusId" -> new AttributeValue(tweetTuple.tweet.statusId.toString),
                  "datetime" -> new AttributeValue(tweetTuple.tweet.datetime),
                  "sentiment" -> new AttributeValue(tweetTuple.sentiment.sentimentValue.toString)
                ).asJava
              )
            )//,
            //new WriteRequest(new PutRequest().withItem((keyMap("B", 1) + ("data" -> S(data(1)))).asJava)),
          ).asJava
      ).asJava
    )
    client.single(batchWriteItemRequest)
  }

}
