package com.z0rchain

import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.MultiUserChat

/**
 * @author ${user.name}
 */
object App {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {
    val config = new ConnectionConfiguration("z0rchain.com");
    config.setCompressionEnabled(true);
    config.setSASLAuthenticationEnabled(true);

    val connection = new XMPPConnection(config);
    connection.connect();
    connection.login("test", "test", "test");

    val muc = new MultiUserChat(connection, "test@conference.z0rchain.com")

    muc.join("testbot")
  }

}
