package com.z0rchain.jabber.hook

import org.jivesoftware.smack.packet.Message

class ListenHook(handler: ListenHook.Handler) extends Hook {

  def handleMessage(message: Message): Option[String] = {
    handler(message.getBody(), message.getFrom())
    None
  }
}
 
object ListenHook {
  type Handler = (/*message:*/ String, /*from:*/ String) => Unit

  def apply(handler: Handler) = new ListenHook(handler)
}
