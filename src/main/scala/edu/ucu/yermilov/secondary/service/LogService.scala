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
    var i = 0
    var consecutive = true
    while (i < logs.length && consecutive) {
      logger.info(s"I = $i, time = ${logs(i).time}, lastLogToShow = $lastLogToShow")
      if (logs(i).time != lastLogToShow + 1) {
        consecutive = false
        lastLogToShow = i
      } else {
        lastLogToShow += 1
      }
      i += 1
    }
    logger.info(s"Is consecutive: $consecutive, lastLogToShow = $lastLogToShow, logs = $allLogs")
    if (consecutive) AllLogs(logs)
    else AllLogs(logs.take(lastLogToShow))
  }
}
