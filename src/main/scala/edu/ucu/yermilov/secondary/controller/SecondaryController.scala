package edu.ucu.yermilov.secondary.controller

import edu.ucu.yermilov.secondary.service.LogService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, ResponseBody}

import scala.beans.BeanProperty

@Controller
class AppController(logService: LogService) {
  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestBody log: Log): Unit = {
    logService.append(log)
  }

  @RequestMapping(value = Array("/messages"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    logService.getAllLogs()
  }
}

case class AllLogs(@BeanProperty logs: Array[Log])

case class Log(@BeanProperty log: String)
