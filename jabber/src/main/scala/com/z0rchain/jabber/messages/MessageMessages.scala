package com.z0rchain.jabber.messages

import com.z0rchain.jabber.JID
import scala.xml.NodeSeq

case class MUCMessage(from: JID, to: JID, body: String)