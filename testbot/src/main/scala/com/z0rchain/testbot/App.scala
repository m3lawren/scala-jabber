package com.z0rchain.testbot

import com.z0rchain.jabber.hook.{CommandHook, HookListener, ListenHook}

import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.{Presence, Message}
import org.jivesoftware.smackx.muc.{DiscussionHistory, MultiUserChat}

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import org.rogach.scallop.ScallopConf

class CmdLine(args: Seq[String]) extends ScallopConf(args) {
  val config = opt[String](required = true)
}

object App {

  val _logger = LoggerFactory.getLogger(getClass)

  def main(args : Array[String]) {

    val cmdLine = new CmdLine(args)

    val propsFile = new File(cmdLine.config())

    if (!propsFile.exists) {
      _logger.error("Properties file %s does not exist. Please create it based off of the sample config file.", propsFile.getCanonicalPath)
      return
    }

    val botConfig = BotConfig.loadFromFile(propsFile)

    val config = new ConnectionConfiguration(botConfig.domain)
    config.setCompressionEnabled(true)
    config.setSASLAuthenticationEnabled(true)


    val connection = new XMPPConnection(config)
    connection.connect()
    connection.login(botConfig.user, botConfig.password, botConfig.resource)

    _logger.info("connected")

    val muc = new MultiUserChat(connection, "test@conference.z0rchain.com")
    val listener = new HookListener(muc)
    val listener2 = new HookListener(muc)

    connection.addPacketListener(listener, new PacketTypeFilter(classOf[Message]))
    connection.addPacketListener(listener2, new PacketTypeFilter(classOf[Presence]))

    val history = new DiscussionHistory
    history.setMaxStanzas(0)

    muc.join("ScalaBot", "", history, 100000)

    _logger.info("muc joined")
    
    muc.sendMessage("Neeeeerds!")

    Thread.sleep(100000)

    muc.sendMessage("And away I go!")
  }
}
