package edu.ucu.yermilov.secondary.service

import edu.ucu.yermilov.secondary.controller.{AllLogs, Log}
import org.slf4j.LoggerFactory

import scala.util.Random

class LogService {

  var allLogs: Array[Log] = Array.empty

  private val logger = LoggerFactory.getLogger(classOf[LogService])

  def append(log: Log): Unit = {
    Thread.sleep(Random.nextInt(500))
    allLogs = allLogs ++ Seq(log)
    logger.info(allLogs.toSeq.mkString(", "))
  }

  def getAllLogs(): AllLogs = AllLogs(allLogs)
}
