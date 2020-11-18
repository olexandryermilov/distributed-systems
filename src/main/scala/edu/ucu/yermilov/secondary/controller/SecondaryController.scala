package edu.ucu.yermilov.secondary.controller

import edu.ucu.yermilov.secondary.service.LogService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}

import scala.beans.BeanProperty

@Controller
class AppController(logService: LogService) {
  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestParam(value = "log", required = true) log: String): Unit = {
    logService.append(Log(log))
  }

  @RequestMapping(value = Array("/messages"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    logService.getAllLogs()
  }
}

case class AllLogs(@BeanProperty logs: Array[Log])

case class Log(@BeanProperty log: String)
