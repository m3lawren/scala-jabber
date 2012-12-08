package com.z0rchain.jabber

import org.jivesoftware.smack._
import org.jivesoftware.smack.packet._

import com.codahale.logula.Logging

class DummyListener extends PacketListener with Logging {
  def processPacket(packet: Packet) = {
    log.info(packet.toXML)
  }
}

