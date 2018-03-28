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
    val actualStatus = if(status.isRetweet) {
      status.getRetweetedStatus
    } else {
      status
    }
    val url = s"https://twitter.com/${actualStatus.getUser.getScreenName}/status/${actualStatus.getId}"
    val mediaUrls = actualStatus.getMediaEntities.map(e => s"${e.getMediaURLHttps}:orig")
    //logger.info(s"onStatus: $url , ${mediaUrls.mkString(", ")}")
  }

  override def onException(ex: Exception): Unit = {
    logger.info("an exception thrown")
  }
}
