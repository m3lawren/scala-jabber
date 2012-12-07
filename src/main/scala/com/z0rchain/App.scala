package com.z0rchain

import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.MultiUserChat

import com.codahale.logula.Logging

/**
 * @author ${user.name}
 */
object App extends Logging {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {

    Logging.configure()

    val config = new ConnectionConfiguration("z0rchain.com");
    config.setCompressionEnabled(true);
    config.setSASLAuthenticationEnabled(true);

    val connection = new XMPPConnection(config);
    connection.connect();
    connection.login("test", "test", "test");

    log.info("connected")

    val muc = new MultiUserChat(connection, "test@conference.z0rchain.com")

    muc.join("ScalaBot")

    log.info("muc joined")
    
    muc.sendMessage("Neeeeerds!")

    Thread.sleep(1000)
  }
}
