package com.z0rchain.jabber.hook

import org.jivesoftware.smack.packet.Message

trait Hook {
  def handleMessage(message: Message): Option[String]
}
