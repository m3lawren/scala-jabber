package com.z0rchain

import org.jivesoftware.smack._

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

      connection.connect();
  }

}
