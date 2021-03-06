package edu.ucu.yermilov.master.controller

import java.util.UUID
import java.util.concurrent.CountDownLatch

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, ResponseBody}
import scalaj.http.Http

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import edu.ucu.yermilov.secondary.controller.{AppendRequest => SecondaryAppendRequest, Log => SecondaryLog}

@Controller
class MasterController(objectMapper: ObjectMapper) {
  implicit val executionContext = ExecutionContext.global
  //private val url = "http://docker.for.mac.localhost:"
  private val url = "http://localhost:"
  private val ports = Seq(9081, 9082)
  private val logger = LoggerFactory.getLogger(classOf[MasterController])
  private val messages: mutable.Map[String, SecondaryLog] = mutable.Map.empty
  private var time: Int = 0

  @RequestMapping(value = Array("/append"), method = Array(RequestMethod.POST))
  @ResponseBody def append(@RequestBody request: AppendRequest): HttpStatus = {
    val availableNodes = {
      for {
        port <- ports
        healthStatus = sendHeartbeat(port)
      } yield {
        if (healthStatus == HttpStatus.OK) 1 else 0
      }
    }.sum
    if (availableNodes > 0) {
      val acks: CountDownLatch = new CountDownLatch(request.writeConcern - 1)
      time += 1
      val messageId = UUID.randomUUID().toString
      val logToAppend = SecondaryLog(request.log, time)
      messages += messageId -> logToAppend
      for (port <- ports) {
        callWithRetry(logToAppend, messageId, port, acks)
      }
      acks.await()
      logger.info("MASTER: Returning result")
      HttpStatus.OK
    }
    else HttpStatus.SERVICE_UNAVAILABLE
  }

  private def callWithRetry(logToAppend: SecondaryLog, messageId: String, port: Int, acks: CountDownLatch): Unit = Future {
    Try {
      Http.apply(s"$url$port/append").header("content-type", "application/json")
        .postData(objectMapper.writeValueAsString(SecondaryAppendRequest(logToAppend, messageId)))
        .execute[HttpStatus](objectMapper.readValue(_, classOf[HttpStatus])).body
    }.get
  }.onComplete {
    case Success(value) =>
      logger.info(s"MASTER: Response from $port is $value")
      if (value == HttpStatus.OK)
        acks.countDown()
      else callWithRetry(logToAppend, messageId, port, acks)
    case Failure(exception) =>
      logger.error(exception.getMessage, exception)
      var heartBeatResponse = sendHeartbeat(port)
      var heartBeatRetries = 1
      logger.info(s"Heartbeat returned $heartBeatResponse")
      var isInstanceAlive = heartBeatResponse == HttpStatus.OK
      while (!isInstanceAlive) {
        heartBeatResponse = sendHeartbeat(port)
        logger.info(s"Heartbeat returned $heartBeatResponse")
        isInstanceAlive = heartBeatResponse == HttpStatus.OK
        Thread.sleep(200)
        heartBeatRetries += 1
      }
      callWithRetry(logToAppend, messageId, port, acks)
  }

  @RequestMapping(value = Array("/readAll"), method = Array(RequestMethod.GET))
  @ResponseBody def readAll(): AllLogs =
    AllLogs(messages.values.toArray.map(x => Log(x.log, x.time)))

  @RequestMapping(value = Array("/health"), method = Array(RequestMethod.GET))
  @ResponseBody def health(): String =
    objectMapper.writeValueAsString(
      for {
        port <- ports
        healthStatus = sendHeartbeat(port)
      } yield {
        s"Instance on $port has status $healthStatus"
      }
    )

  private def sendHeartbeat(port: Int): HttpStatus = Try {
    Http.apply(s"$url$port/health")
      .copy(method = "GET")
      .execute[HttpStatus](objectMapper.readValue(_, classOf[HttpStatus])).body
  }.getOrElse(HttpStatus.SERVICE_UNAVAILABLE)
}

case class AllLogs(@BeanProperty logs: Array[Log]) {
  override def toString: String = s"[${logs.toSeq.mkString(", ")}]"
}

case class Log(@BeanProperty log: String, @BeanProperty time: Int)

case class AppendRequest(@BeanProperty log: String, @BeanProperty writeConcern: Int)
