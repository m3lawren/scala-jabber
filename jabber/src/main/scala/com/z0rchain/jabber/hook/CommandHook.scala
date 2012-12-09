package com.z0rchain.jabber.hook

import org.jivesoftware.smack.packet._

class CommandHook(command: String, handler: (String, String) => Option[String]) extends Hook {
  val _command = """^%s\s*([^\s].*[^\s]?)\s*""".format(command).r

  def handleMessage(message: Message): Option[String] = {
    message.getBody() match {
      case _command(msg) => handler(msg, message.getFrom())
      case _ => None
    }
  }
}
