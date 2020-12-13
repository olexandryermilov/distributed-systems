package edu.ucu.yermilov.secondary.controller

import edu.ucu.yermilov.secondary.service.LogService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, ResponseBody}

import scala.beans.BeanProperty

@Controller
class AppController(logService: LogService) {

  private val logger = LoggerFactory.getLogger(classOf[AppController])

  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestBody log: Log): HttpStatus = {
    logger.info(s"SECONDARY: Got message $log at secondary")
    logService.append(log)
    HttpStatus.OK
  }

  @RequestMapping(value = Array("/messages"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    val result = logService.getAllLogs()
    logger.info(s"SECONDARY: Got request to return all logs at secondary, returning ${result.toString}")
    result
  }
}

case class AllLogs(@BeanProperty logs: Array[Log]) {
  override def toString: String = s"[${logs.toSeq.mkString(", ")}]"
}

case class Log(@BeanProperty log: String)
