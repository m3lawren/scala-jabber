package com.z0rchain.testbot

import akka.actor.{Props, ActorSystem}
import com.z0rchain.jabber.actors.{MessageActor, RosterActor}
import com.z0rchain.jabber.hook.HookListener
import java.io.File
import org.jivesoftware.smack.filter.{OrFilter, PacketTypeFilter}
import org.jivesoftware.smack.packet.{Presence, Message}
import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.{DiscussionHistory, MultiUserChat}
import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory


class CmdLine(args: Seq[String]) extends ScallopConf(args) {
  val config = opt[String](required = true)
}

object App {

  val _logger = LoggerFactory.getLogger(getClass)

  def main(args : Array[String]) {

    val cmdLine = new CmdLine(args)

    val propsFile = new File(cmdLine.config())

    if (!propsFile.exists) {
      _logger.error("Properties file %s does not exist. Please create it based off of the sample config file.".format(propsFile.getCanonicalPath))
      return
    }

    val botConfig = BotConfig.loadFromFile(propsFile)

    val config = new ConnectionConfiguration(botConfig.domain)
    config.setCompressionEnabled(true)
    config.setSASLAuthenticationEnabled(true)

    val actorSystem = ActorSystem("ScalaBot")
    val rosterActor = actorSystem.actorOf(Props[RosterActor])
    val messageActor = actorSystem.actorOf(Props[MessageActor])

    _logger.info("Connecting as %s@%s/%s...".format(botConfig.user, botConfig.domain, botConfig.resource))

    val connection = new XMPPConnection(config)
    connection.connect()
    connection.login(botConfig.user, botConfig.password, botConfig.resource)

    _logger.info("Connected.")

    _logger.info("Joining channel %s...".format(botConfig.channel))

    val muc = new MultiUserChat(connection, botConfig.channel)
    val listener = new HookListener(muc, rosterActor, messageActor)

    connection.addPacketListener(listener, new OrFilter(new PacketTypeFilter(classOf[Message]), new PacketTypeFilter(classOf[Presence])))

    val history = new DiscussionHistory
    history.setMaxStanzas(0)

    muc.join(botConfig.nick, "", history, 100000)

    _logger.info("Channel %s joined.".format(botConfig.channel))
    
    muc.sendMessage("Sup nerds")

    actorSystem.awaitTermination()
    _logger.info("Shutdown successful.")
  }
}