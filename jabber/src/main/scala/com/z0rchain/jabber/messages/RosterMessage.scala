package com.z0rchain.jabber.messages

import com.z0rchain.jabber.JID

case class RosterJoin(channel: String, jid: JID, nick: String)
case class RosterPart(channel: String, jid: JID, nick: String)
case class RosterGet(channel: String)
case object RosterDump

