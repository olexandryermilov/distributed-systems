name := "distributed-systems"

version := "0.1"

scalaVersion := "2.12.3"


libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "1.5.4.RELEASE",
  "org.springframework.boot" % "spring-boot-configuration-processor" % "1.5.4.RELEASE",
  "org.scalaj" %% "scalaj-http" % "2.4.1",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.9.8",
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.slf4j" % "slf4j-simple" % "1.7.30",
)