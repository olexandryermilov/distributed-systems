package edu.ucu.yermilov.master

import edu.ucu.yermilov.master.controller.AppController
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

object Master {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Master], args: _ *)
  }

}

@SpringBootApplication
class Master {
  @Bean
  def appController = new AppController()
}
