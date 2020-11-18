package edu.ucu.yermilov.master.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}
import scalaj.http.Http

import scala.beans.BeanProperty

@Controller
class AppController() {
  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestParam(value = "log", required = true) log: String): Unit = {
    val resp1 = Http.apply("http://localhost:9081/append").copy(
      method = "POST",
      params = Seq("log" -> log)
    ).execute()
    val resp2 = Http.apply("http://localhost:9082/append").copy(
      method = "POST",
      params = Seq("log" -> log)
    ).execute()
  }

  @RequestMapping(value = Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val resp = Http.apply("http://localhost:9081/messages").copy(
      method = "GET",
    ).execute[AllLogs](mapper.readValue(_, classOf[AllLogs])).body

    //mapper.readValue(resp, )
    resp
  }
}

case class AllLogs(@BeanProperty logs: Array[Log])

case class Log(@BeanProperty log: String)
