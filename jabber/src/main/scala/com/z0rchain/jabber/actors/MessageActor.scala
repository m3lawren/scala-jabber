package com.z0rchain.jabber.actors

import akka.actor.Actor
import org.slf4j.LoggerFactory
import org.jivesoftware.smack.packet.Packet
import com.z0rchain.jabber.commands.{BasicCommand, BasicCommandParser}
import com.z0rchain.jabber.messages.MUCMessage

class MessageActor extends Actor {
  private val _logger = LoggerFactory.getLogger(getClass)

  def receive = {
    case BasicCommandParser(BasicCommand("quit")) =>
      context.system.shutdown
    case BasicCommandParser(BasicCommand(cmd)) =>
      _logger.info("Unknown command received: %s".format(cmd))
    case x: Packet =>
      _logger.warn("Unknown packet received: %s".format(x.toXML))
    case x =>
      _logger.warn("Unknown message received: %s".format(x))
  }

}
