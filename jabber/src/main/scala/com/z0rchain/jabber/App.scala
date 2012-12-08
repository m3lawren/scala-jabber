package com.z0rchain.jabber

import org.jivesoftware.smack.{ConnectionConfiguration, PacketListener, XMPPConnection}
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.{Message, Packet}
import org.jivesoftware.smackx.muc.{DiscussionHistory, MultiUserChat}

import com.codahale.logula.Logging

class DummyListener extends PacketListener with Logging {
  def processPacket(packet: Packet) = {
    log.info(packet.toXML)
  }
}

/**
 * @author ${user.name}
 */
object App extends Logging {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {

    Logging.configure()

    val config = new ConnectionConfiguration("z0rchain.com")
    config.setCompressionEnabled(true)
    config.setSASLAuthenticationEnabled(true)

    val listener = new DummyListener

    val connection = new XMPPConnection(config)
    connection.addPacketListener(listener, new PacketTypeFilter(classOf[Message]))

    connection.connect()
    connection.login("test", "test", "test")

    log.info("connected")

    val history = new DiscussionHistory
    history.setMaxStanzas(0)

    val muc = new MultiUserChat(connection, "test@conference.z0rchain.com")

    muc.join("ScalaBot", "", history, 100000)

    log.info("muc joined")
    
    muc.sendMessage("Neeeeerds!")

    Thread.sleep(10000)
  }
}
