package edu.ucu.yermilov.master

import org.springframework.boot.SpringApplication

object Master {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Master], args: _ *)
  }

}

@SpringBootApplication
class Master {

}
