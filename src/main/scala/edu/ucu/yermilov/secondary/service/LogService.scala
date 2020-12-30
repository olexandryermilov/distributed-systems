package edu.ucu.yermilov.secondary.service

import edu.ucu.yermilov.secondary.controller.{AllLogs, Log}
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.util.Random

class LogService {

  var allLogs: mutable.Map[String, Log] = mutable.Map.empty

  private val logger = LoggerFactory.getLogger(classOf[LogService])

  def append(log: Log, messageId: String): Unit = {
    Thread.sleep(Random.nextInt(500))
    if(!allLogs.contains(messageId))
      allLogs += Seq(log)
    logger.info(allLogs.toSeq.mkString(", "))
  }

  def getAllLogs(): AllLogs = AllLogs(allLogs.values.toArray)
}
