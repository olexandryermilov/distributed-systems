package edu.ucu.yermilov.master.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
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
  @ResponseBody def append(@RequestBody log: Log): HttpStatus = {
    var result = true
    for (port <- ports) {
      val response: HttpStatus = Http.apply(s"$url$port/append").header("content-type", "application/json").postData(objectMapper.writeValueAsString(log)).execute[HttpStatus](objectMapper.readValue(_, classOf[HttpStatus])).body
      println(s"Response from $port is $response")
      result = result && response == HttpStatus.OK
     }
    if(result)
      HttpStatus.OK
    else
      HttpStatus.INTERNAL_SERVER_ERROR
  }

  @RequestMapping(value = Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    val resp1 = Http.apply(s"${url}9081/messages").copy(
      method = "GET",
    ).execute[AllLogs](objectMapper.readValue(_, classOf[AllLogs])).body
    println(s"Response from 9081 is ${resp1.toString}")
    val resp2 = Http.apply(s"${url}9082/messages").copy(
      method = "GET",
    ).execute[AllLogs](objectMapper.readValue(_, classOf[AllLogs])).body
    println(s"Response from 9082 is ${resp2.toString}")
    if(resp1.logs.deep == resp2.logs.deep) resp1 else throw new RuntimeException("Answers from secondaries are not equal")
  }
}

case class AllLogs(@BeanProperty logs: Array[Log]) {
  override def toString: String = s"[${logs.toSeq.mkString(", ")}]"
}

case class Log(@BeanProperty log: String)
