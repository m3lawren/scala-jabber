package com.z0rchain.jabber.hook

import org.jivesoftware.smack._
import org.jivesoftware.smack.packet._
import org.jivesoftware.smackx.muc._

class HookListener(chat: MultiUserChat) extends PacketListener {

  private var hooks: Seq[Hook] = Seq()

  def addHook(hook: Hook) = {
    hooks = hooks ++ Seq(hook)
  }

  def processPacket(packet: Packet) = {
    packet match {
      case message: Message => 
        hooks.foreach(hook => processHook(hook, message))
      case _ => {}
    }
  }

  def processHook(hook: Hook, message: Message) = {
    hook.handleMessage(message) match {
      case Some(str) => {
        chat.sendMessage(str)
      }
      case None => {}
    }
  }
}
