package com.soujiro0725.twitter

import com.typesafe.scalalogging.Logger
import twitter4j.{
  StatusListener,
  Status,
  StatusDeletionNotice,
  StallWarning
}

class StatusListenerImpl extends StatusListener {

  private[this] val logger = Logger[StatusListenerImpl]

  override def onStallWarning(warning: StallWarning): Unit = {
    logger.info("warning ")
  }

  override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {
    logger.info("deletion ")
  }

  override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {
    logger.info("geo...")
  }

  override def onTrackLimitationNotice(numberOfLimitedStatus: Int): Unit = {
    logger.info("on tack...")
  }

  override def onStatus(status: Status): Unit = {
    logger.info("onStatus running...")
  }

  override def onException(ex: Exception): Unit = {
    logger.info("an exception thrown")
  }
}
