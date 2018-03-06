package com.soujiro0725.twitter

import com.typesafe.scalalogging.Logger
import twitter4j.{StatusListener}

class StatusListenerImpl() extends StatusListener {

  private[this] val logger = Logger[StatusListenerImpl]

  override def onStatus(): Unit = {
    
  }
}
