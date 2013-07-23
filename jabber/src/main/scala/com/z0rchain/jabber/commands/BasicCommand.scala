package com.z0rchain.jabber.commands

import com.z0rchain.jabber.messages.MUCMessage
import org.slf4j.LoggerFactory

case class BasicCommand(command: String)

object BasicCommandParser {
  private val _logger = LoggerFactory.getLogger(getClass)
  private val commandRex = """^\s*!(\w*)\s*$""".r

  def unapply(m: MUCMessage): Option[BasicCommand] = {
    m.body match {
      case commandRex(command) => Some(BasicCommand(command))
      case _ => None
    }
  }
}
