package com.z0rchain.jabber.hook

import org.jivesoftware.smack.packet._

trait Hook {
  def handleMessage(message: Message): Option[String]
}
