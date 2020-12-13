package edu.ucu.yermilov.secondary.service

import edu.ucu.yermilov.secondary.controller.{AllLogs, Log}

import scala.util.Random

class LogService {

  var allLogs: Array[Log] = Array.empty

  def append(log: Log): Unit = {
    Thread.sleep(Random.nextInt(500))
    allLogs = allLogs ++ Seq(log)
    println(allLogs.toSeq.mkString(", "))
  }

  def getAllLogs(): AllLogs = AllLogs(allLogs)
}
