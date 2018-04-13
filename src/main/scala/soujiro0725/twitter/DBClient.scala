package com.soujiro0725.twtiter

import akka.stream.alpakka.dynamodb.impl.DynamoSettings
import akka.stream.alpakka.dynamodb.scaladsl._
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.client.builder.AwsClientBuilder

object DBClient {

  val db_host = "http://localhost" 
  val db_port = "32768"

  val client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
    new AwsClientBuilder.EndpointConfiguration(db_host + ":" + db_port, "ap-northeast-1")
  )
}
