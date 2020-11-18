package edu.ucu.yermilov.secondary.service

import edu.ucu.yermilov.secondary.controller.{AllLogs, Log}

class LogService {

  var allLogs: Array[Log] = Array.empty

  def append(log: Log): Unit = {
    allLogs = allLogs ++ Seq(log)
    println(allLogs)
  }

  def getAllLogs(): AllLogs = AllLogs(allLogs)
}
