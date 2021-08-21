name := "kafka_client"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "2.6.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
    // https://mvnrepository.com/artifact/org.apache.commons/commons-csv
  "org.apache.commons" % "commons-csv" % "1.8",
  "org.scala-lang" % "scala-library" % "2.10.0-RC2",
  // https://mvnrepository.com/artifact/com.typesafe.play/play-json
  "com.typesafe.play" %% "play-json" % "2.9.2"



)
