import com.typesafe.sbt.packager.docker._

enablePlugins(JavaAppPackaging, DockerPlugin)

organization := "org.renci"

name := "sparql-kp"

version := "0.1"

licenses := Seq("MIT license" -> url("https://opensource.org/licenses/MIT"))

scalaVersion := "2.13.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

javaOptions += "-Xmx8G"

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

val zioVersion = "1.0.4-2"
val zioConfigVersion = "1.0.0-RC26"
val tapirVersion = "0.16.16"
val http4sVersion = "0.21.16"
val circeVersion = "0.13.0"
val logbackVersion = "1.2.3"

javaOptions in reStart += "-Xmx16G"

libraryDependencies ++= {
  Seq(
    "dev.zio"                     %% "zio"                      % zioVersion,
    "dev.zio"                     %% "zio-interop-cats"         % "2.2.0.1",
    "dev.zio"                     %% "zio-config"               % zioConfigVersion,
    "dev.zio"                     %% "zio-config-magnolia"      % zioConfigVersion,
    "dev.zio"                     %% "zio-config-typesafe"      % zioConfigVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-zio"                % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % tapirVersion,
    "org.http4s"                  %% "http4s-blaze-server"      % http4sVersion,
    "org.http4s"                  %% "http4s-dsl"               % http4sVersion,
    "org.http4s"                  %% "http4s-blaze-client"      % http4sVersion,
    "org.http4s"                  %% "http4s-circe"             % http4sVersion,
    "org.apache.jena"              % "apache-jena-libs"         % "3.16.0",
    "org.phenoscape"              %% "sparql-utils"             % "1.3",
    "org.apache.commons"           % "commons-text"             % "1.9",
    "commons-codec"                % "commons-codec"            % "1.15",
    "io.circe"                    %% "circe-core"               % circeVersion,
    "io.circe"                    %% "circe-generic"            % circeVersion,
    "io.circe"                    %% "circe-parser"             % circeVersion,
    "io.circe"                    %% "circe-yaml"               % "0.13.1",
    "dev.zio"                     %% "zio-test"                 % zioVersion % Test,
    "dev.zio"                     %% "zio-test-sbt"             % zioVersion % Test,
    "ch.qos.logback"               % "logback-classic"          % logbackVersion,
    "com.typesafe.scala-logging"  %% "scala-logging"            % "3.9.2"
  )
}

dockerBaseImage := "openjdk:14-alpine"
daemonUser in Docker := "sparqlkp"
dockerExposedPorts += 8080
dockerApiVersion := Some(DockerApiVersion(1, 40))
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerCommands := dockerCommands.value.flatMap {
  case cmd @ Cmd("EXPOSE", _) => List(Cmd("RUN", "apk update && apk add bash"), cmd)
  case other => List(other)
}
