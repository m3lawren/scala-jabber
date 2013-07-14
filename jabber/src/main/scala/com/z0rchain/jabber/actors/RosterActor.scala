package com.z0rchain.jabber.actors

import akka.actor.Actor
import org.slf4j.LoggerFactory
import com.z0rchain.jabber.messages.{RosterInit, RosterPart, RosterJoin}

class RosterActor extends Actor {
  val _logger = LoggerFactory.getLogger(getClass)
  var _channel: Option[String] = None

  private def updateChannel(channel: String) = {
    _channel match {
      case None =>
        _logger.info("Associating with channel %s.".format(channel))
        _channel = Some(channel)
      case Some(c) if c != channel =>
        _logger.error("Received status for channel %s but was expecting %s".format(channel, c))
      case _ =>
    }
  }

  def receive = {
    case m: RosterInit =>
      updateChannel(m.channel)
    case m: RosterJoin =>
      _logger.info("User %s joined channel %s as %s".format(m.jid, m.channel, m.nick))
      updateChannel(m.channel)
    case m: RosterPart =>
      _logger.info("User %s parted channel %s as %s".format(m.jid, m.channel, m.nick))
      updateChannel(m.channel)
    case x =>
      _logger.warn("Unknown message received: %s".format(x))
  }
}
