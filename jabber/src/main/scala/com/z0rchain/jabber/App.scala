package com.z0rchain.jabber

import com.z0rchain.jabber.hook._

import org.jivesoftware.smack._
import org.jivesoftware.smack.filter._
import org.jivesoftware.smack.packet._
import org.jivesoftware.smackx.muc._

import com.codahale.logula.Logging

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


    val connection = new XMPPConnection(config)
    connection.connect()
    connection.login("test", "test", "test")

    log.info("connected")

    val muc = new MultiUserChat(connection, "test@conference.z0rchain.com")
    val listener = new HookListener(muc)

    listener.addHook(new CommandHook("!echo", {(message: String, sender: String) =>
      Some(message)
    }))

    connection.addPacketListener(listener, new PacketTypeFilter(classOf[Message]))


    val history = new DiscussionHistory
    history.setMaxStanzas(0)


    muc.join("ScalaBot", "", history, 100000)

    log.info("muc joined")
    
    muc.sendMessage("Neeeeerds!")

    Thread.sleep(100000)
  }
}
