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

/**
  * export AWS_CREDENTIAL_PROFILES_FILE=~/.aws/credentials
  * 
  * aws dynamodb create-table --table-name Persons \
  * --attribute-definitions AttributeName=Id,AttributeType=N \
  * --key-schema AttributeName=Id,KeyType=HASH \
  * --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
  * --endpoint-url http://localhost:32768
  */
object DBClient {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val settings = DynamoSettings(system)
  val client = DynamoClient(settings)

  def createTable(tableName: String) {

    val keyCol = "kkey"
    val sortCol = "sort"

    val createTableRequest = new CreateTableRequest()
      .withTableName(tableName)
      .withKeySchema(
        new KeySchemaElement().withAttributeName(keyCol).withKeyType(KeyType.HASH),
        new KeySchemaElement().withAttributeName(sortCol).withKeyType(KeyType.RANGE)
      )
      .withAttributeDefinitions(
        new AttributeDefinition().withAttributeName(keyCol).withAttributeType("S"),
        new AttributeDefinition().withAttributeName(sortCol).withAttributeType("N")
      )
      .withProvisionedThroughput(
        new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(10L)
      )

    client.single(createTableRequest)
  }

  def listTable() {
    client.single(new ListTablesRequest())
  }

  def update(data: String) {
    println("printing table info...")

    // val table = DescribeTable 
    // println(client.describeTable)
    // println("printing data...")
    // println(data)
  }

}
