package edu.ucu.yermilov.master

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import edu.ucu.yermilov.master.controller.MasterController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}

object Master {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Master], args: _ *)
  }

}

@SpringBootApplication
class Master {
  @Bean
  def masterController(objectMapper: ObjectMapper) = new MasterController(objectMapper)
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
