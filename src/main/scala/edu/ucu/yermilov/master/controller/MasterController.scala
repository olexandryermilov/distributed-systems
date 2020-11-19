package edu.ucu.yermilov.master.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, ResponseBody}
import scalaj.http.Http

import scala.beans.BeanProperty

@Controller
class MasterController(objectMapper: ObjectMapper) {
  private val url = "http://docker.for.mac.localhost:"
  //private val url = "http://localhost:"
  private val ports = Seq(9081, 9082)
  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestBody log: Log): Unit = {
    for {
      port <- ports
    }
    yield Http.apply(s"$url$port/append").header("content-type", "application/json").postData(objectMapper.writeValueAsString(log)).execute()
  }

  @RequestMapping(value = Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    lazy val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val resp = Http.apply(s"${url}9081/messages").copy(
      method = "GET",
    ).execute[AllLogs](mapper.readValue(_, classOf[AllLogs])).body

    resp
  }
}

case class AllLogs(@BeanProperty logs: Array[Log])

case class Log(@BeanProperty log: String)
