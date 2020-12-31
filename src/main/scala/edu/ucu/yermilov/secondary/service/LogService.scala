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
    if (!allLogs.contains(messageId))
      allLogs += messageId -> log
    logger.info(allLogs.toSeq.mkString(", "))
  }

  def getAllLogs(): AllLogs = {
    val logs = allLogs.values.toArray.sortBy(_.time)
    var lastLogToShow = 0
    var i = 1
    var consecutive = true
    while (i < logs.length && consecutive) {
      if (logs(i).time != logs(i - 1).time + 1) {
        consecutive = false
        lastLogToShow = i - 1
      }
      i += 1
    }
    AllLogs(logs.take(lastLogToShow + 1))
  }
}
