package com.z0rchain.testbot

import java.util.Properties
import org.slf4j.LoggerFactory
import java.io.{File, FileInputStream}

class BotConfig(props: Properties) {
  def getOpt(key: String): Option[String] = {
    Option(props.getProperty(key))
  }

  lazy val domain = getOpt("domain").getOrElse(sys.error("No domain specified"))
  lazy val user = getOpt("user").getOrElse(sys.error("No user specified"))
  lazy val password = getOpt("password").getOrElse(sys.error("No password specified"))
  lazy val resource = getOpt("resource").getOrElse(sys.error("No resource specified"))

  lazy val channel = getOpt("channel").getOrElse(sys.error("No channel specified"))
  lazy val nick = getOpt("nick").getOrElse(resource)
}


object BotConfig {
  val _logger = LoggerFactory.getLogger(getClass)

  def loadFromFile(file: File) = {
    _logger.info("Loading config from %s".format(file.toString))
    val props = new Properties
    props.load(new FileInputStream(file))

    new BotConfig(props)
  }
}