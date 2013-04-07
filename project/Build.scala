import java.io.PrintWriter
import sbt._
import Keys._

object Resolvers {
  val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
}

object BuildSettings {
  import Resolvers._

  val buildOrganization = "com.z0rchain"
  val buildVersion = "1.0"
  val buildScalaVersion = "2.10.0"

  val WriteClasspath = config("writeClasspath")
  val writeClasspath = TaskKey[Unit]("writeClasspath")
  val writeClasspathTask = writeClasspath <<= (target, fullClasspath in Runtime) map { (target, cp) =>
    def writeFile(file: File, str: String) {
      val writer = new PrintWriter(file)
      writer.println(str)
      writer.close()
    }
    val cpString = cp.map(_.data).mkString(":")
    val targetFile = (target / ".." / "cp.txt").asFile
    writeFile(targetFile, cpString)
  }

  val buildSettings = Defaults.defaultSettings ++ Seq(writeClasspathTask) ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalacOptions := Seq("-deprecation", "-unchecked"),
    resolvers ++= Seq(typesafe)
  )
}

object Dependencies {
  val smack = "org.igniterealtime.smack" % "smack" % "3.2.1"
  val smackx = "org.igniterealtime.smack" % "smackx" % "3.2.1"
  val log4j = "log4j" % "log4j" % "1.2.17"
  val slf4j_api = "org.slf4j" % "slf4j-api" % "1.7.3"
  val slf4j_log4j = "org.slf4j" % "slf4j-log4j12" % "1.7.3"
  val akka = "com.typesafe.akka" %% "akka-actor" % "2.1.2"
}

object JabberBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  lazy val root = Project(
    id           = "scala-jabber",
    base         = file("."),
    settings     = buildSettings,
    aggregate    = Seq(testbot)
  )

  lazy val jabber = Project(
    id           = "jabber", 
    base         = file("jabber"),
    settings     = buildSettings ++ Seq(libraryDependencies ++= Seq(smack, smackx, akka))
  ) 

  lazy val testbot = Project(
    id           = "testbot", 
    base         = file("testbot"),
    dependencies = Seq(jabber),
    settings     = buildSettings ++ Seq(libraryDependencies ++= Seq(smack, smackx, slf4j_api, slf4j_log4j))
  ) 
}
