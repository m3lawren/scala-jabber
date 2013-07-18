package com.z0rchain.jabber.actors

import akka.actor.Actor
import org.slf4j.LoggerFactory
import com.z0rchain.jabber.messages.{RosterGet, RosterInit, RosterPart, RosterJoin}
import com.z0rchain.jabber.JID
import collection.mutable.HashSet

case class RosterEntry(nick: String, jid: JID)

class RosterActor extends Actor {
  private val _logger = LoggerFactory.getLogger(getClass)
  private var _channel: Option[String] = None

  private var _roster = HashSet.empty[RosterEntry]

  private def updateChannel(channel: String, f: () => Unit) = {
    _channel match {
      case None =>
        _logger.info("Associating with channel %s.".format(channel))
        _channel = Some(channel)
        f
      case Some(c) if c != channel =>
        _logger.error("Received status for channel %s but was expecting %s".format(channel, c))
      case _ =>
        f
    }
  }

  def receive = {
    case m: RosterInit =>
      updateChannel(m.channel, () => ())

    case m: RosterJoin =>
      _logger.info("User %s joined channel %s as %s".format(m.jid, m.channel, m.nick))
      updateChannel(m.channel, () => {
        _roster += RosterEntry(m.nick, m.jid)
      })

    case m: RosterPart =>
      _logger.info("User %s parted channel %s as %s".format(m.jid, m.channel, m.nick))
      updateChannel(m.channel, () => {
        _roster -= RosterEntry(m.nick, m.jid)
      })

    case RosterGet =>
      sender ! _roster.toSet

    case x =>
      _logger.warn("Unknown message received: %s".format(x))
  }
}
