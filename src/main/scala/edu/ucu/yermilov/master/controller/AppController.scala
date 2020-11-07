package edu.ucu.yermilov.master.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}

import scala.beans.BeanProperty

@Controller
class AppController() {
  @RequestMapping(value =  Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestParam(value = "log", required = true) log: String): Unit = {

  }

  @RequestMapping(value =  Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    AllLogs(Array.empty)
  }
}

case class AllLogs(@BeanProperty logs: Array[Log])

case class Log(@BeanProperty log: String)
