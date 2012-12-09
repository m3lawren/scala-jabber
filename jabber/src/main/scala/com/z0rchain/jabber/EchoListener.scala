package com.z0rchain.jabber

import org.jivesoftware.smack._
import org.jivesoftware.smack.packet._
import org.jivesoftware.smackx.muc._

import com.codahale.logula.Logging

class EchoListener(chat: MultiUserChat) extends PacketListener with Logging {
  def processPacket(packet: Packet) = {
    packet match {
      case message: Message => processMessage(message)
      case _ => {}
    }
  }

  def processMessage(message: Message) = {
    val echoRegex = """^!echo\s+([^\s].*[^\s])\s*$""".r
    message.getBody match {
      case echoRegex(message) => {
        chat.sendMessage(message)
      }
      case _ => {}
    }
  }
}

