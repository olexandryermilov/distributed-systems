package edu.ucu.yermilov.secondary

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import edu.ucu.yermilov.secondary.controller.AppController
import edu.ucu.yermilov.secondary.service.LogService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}

object Secondary {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Secondary], args: _ *)
  }

}

@SpringBootApplication
class Secondary {
  @Bean def logService = new LogService()
  @Bean def appController(logService: LogService) = new AppController(logService)
}

@Configuration
class JacksonConfiguration {

  @Bean
  def objectMapper: ObjectMapper = {
    val objectMapper = new ObjectMapper() //with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper
  }
}