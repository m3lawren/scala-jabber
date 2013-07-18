package com.z0rchain.jabber.actors

import akka.actor.Actor
import org.slf4j.LoggerFactory
import com.z0rchain.jabber.messages.{RosterDump, RosterGet, RosterPart, RosterJoin}
import com.z0rchain.jabber.JID
import collection.mutable.{HashSet, HashMap, Set}

case class RosterEntry(nick: String, jid: JID)

class RosterActor extends Actor {
  private val _logger = LoggerFactory.getLogger(getClass)
  private var _channelRosters = HashMap.empty[String, Set[RosterEntry]]

  private def channelRoster(channel: String): Set[RosterEntry] = {
    if (!_channelRosters.contains(channel))
      _channelRosters += channel -> HashSet.empty[RosterEntry]
    _channelRosters(channel)
  }

  def receive = {
    case RosterJoin(channel, jid, nick) =>
      _logger.info("User %s joined channel %s as %s".format(jid, channel, nick))

      channelRoster(channel) += RosterEntry(nick, jid)

    case RosterPart(channel, jid, nick) =>
      _logger.info("User %s parted channel %s as %s".format(jid, channel, nick))
      channelRoster(channel) -= RosterEntry(nick, jid)

    case RosterGet(channel) =>
      sender ! _channelRosters.getOrElse(channel, Set.empty[RosterEntry])

    case RosterDump =>
      _logger.info("Roster dump...")
      _logger.info(_channelRosters.toString)

    case x =>
      _logger.warn("Unknown message received: %s".format(x))
  }
}
