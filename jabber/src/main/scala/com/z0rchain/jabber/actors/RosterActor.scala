package com.z0rchain.jabber.actors

import akka.actor.Actor
import collection.mutable.{HashSet, HashMap, Set}
import com.z0rchain.jabber.JID
import com.z0rchain.jabber.messages.{RosterDump, RosterGet, RosterPart, RosterJoin}
import concurrent.duration._
import org.slf4j.LoggerFactory

case class RosterEntry(nick: String, jid: JID)

class RosterActor extends Actor {
  private val _logger = LoggerFactory.getLogger(getClass)
  private var _channelRosters = HashMap.empty[String, Set[RosterEntry]]

  private def channelRoster(channel: String): Set[RosterEntry] = {
    if (!_channelRosters.contains(channel))
      _channelRosters += channel -> HashSet.empty[RosterEntry]
    _channelRosters(channel)
  }

  override def preStart = {
    _logger.info("Pre-start.")

    import context.dispatcher

    context.system.scheduler.schedule(0.seconds, 5.seconds, self, RosterDump)
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
      _logger.info("Roster dump: %s".format(_channelRosters))

    case x =>
      _logger.warn("Unknown message received: %s".format(x))
  }
}
