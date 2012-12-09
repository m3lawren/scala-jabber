package com.z0rchain.jabber.hook

import org.jivesoftware.smack.packet._

class CommandHook(command: String, handler: CommandHook.Handler) extends Hook {
  val _command = """^%s\s*([^\s].*[^\s]?)\s*""".format(command).r

  def handleMessage(message: Message): Option[String] = {
    message.getBody() match {
      case _command(msg) => handler(msg, message.getFrom())
      case _ => None
    }
  }
}

object CommandHook {
  type Handler = (/*message:*/ String, /*from:*/ String) => Option[String]

  def apply(command: String, handler: Handler) = new CommandHook(command, handler)
}
