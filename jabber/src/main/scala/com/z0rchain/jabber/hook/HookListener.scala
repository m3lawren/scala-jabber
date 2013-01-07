package com.z0rchain.jabber.hook

import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.packet.{Message, Packet}
import org.jivesoftware.smackx.muc.{MultiUserChat}

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
