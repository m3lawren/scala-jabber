import java.io.PrintWriter
import sbt._
import Keys._

object BuildSettings {

  val buildOrganization = "com.z0rchain"
  val buildVersion = "1.0"
  val buildScalaVersion = "2.9.2"

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
    scalacOptions := Seq("-deprecation", "-unchecked")
  )
}

object Dependencies {
  val smack = "org.igniterealtime.smack" % "smack" % "3.2.1"
  val smackx = "org.igniterealtime.smack" % "smackx" % "3.2.1"
  val log4j = "log4j" % "log4j" % "1.2.17"
}

object JabberBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  lazy val jabber = Project(
    id = "jabber", 
    base = file("."), 
    settings = buildSettings ++ Seq(libraryDependencies ++= Seq(smack, smackx, log4j))) 
}
