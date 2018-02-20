package com.soujiro0725.twitter

import com.typesafe.scalalogging.Logger

object Main {
  def main(args: Array[String]): Unit = {
    val log = Logger(this.getClass.getName)
    println("running main function")
    log.info("logging now...")
  }
}
