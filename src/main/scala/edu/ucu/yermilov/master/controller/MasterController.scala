package edu.ucu.yermilov.master.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, ResponseBody}
import scalaj.http.Http

import scala.beans.BeanProperty
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Controller
class MasterController(objectMapper: ObjectMapper) {
  implicit val executionContext = ExecutionContext.global
  private val url = "http://docker.for.mac.localhost:"
  //private val url = "http://localhost:"
  private val ports = Seq(9081, 9082)
  private val logger = LoggerFactory.getLogger(classOf[MasterController])

  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestBody request: AppendRequest): HttpStatus = {
    var result = true
    var acks = 1
    for (port <- ports) {
      val responses = Future {
        Http.apply(s"$url$port/append").header("content-type", "application/json").postData(objectMapper.writeValueAsString(Log(request.log))).execute[HttpStatus](objectMapper.readValue(_, classOf[HttpStatus])).body
      }.onComplete {
        case Success(value) =>
          logger.info(s"MASTER: Response from $port is $value")
          result = result && value == HttpStatus.OK
          acks += 1
        case Failure(exception) => logger.error(exception.getMessage, exception)
      }
    }
    while (acks < request.writeConcern) {
      print("")
    }
    logger.info("MASTER: Returning result")
    if (result)
      HttpStatus.OK
    else
      HttpStatus.INTERNAL_SERVER_ERROR
  }

  @RequestMapping(value = Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs = {
    val resp1 = Http.apply(s"${url}9081/messages").copy(
      method = "GET",
    ).execute[AllLogs](objectMapper.readValue(_, classOf[AllLogs])).body
    logger.info(s"MASTER: Response from 9081 is ${resp1.toString}")
    val resp2 = Http.apply(s"${url}9082/messages").copy(
      method = "GET",
    ).execute[AllLogs](objectMapper.readValue(_, classOf[AllLogs])).body
    logger.info(s"Response from 9082 is ${resp2.toString}")
    if (resp1.logs.deep == resp2.logs.deep) resp1 else throw new RuntimeException("Answers from secondaries are not equal")
  }
}

case class AllLogs(@BeanProperty logs: Array[Log]) {
  override def toString: String = s"[${logs.toSeq.mkString(", ")}]"
}

case class Log(@BeanProperty log: String)

case class AppendRequest(@BeanProperty log: String, @BeanProperty writeConcern: Int)
