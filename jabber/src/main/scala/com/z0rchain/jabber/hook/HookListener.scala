package com.z0rchain.jabber.hook

import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.packet.{Presence, Message, Packet}
import org.jivesoftware.smackx.muc.{MultiUserChat}
import org.slf4j.LoggerFactory
import org.jivesoftware.smackx.packet.MUCUser
import akka.actor.ActorRef
import com.z0rchain.jabber.messages.{RosterPart, RosterJoin}
import org.jivesoftware.smack.util.StringUtils
import com.z0rchain.jabber.JID

class HookListener(chat: MultiUserChat, rosterActor: ActorRef) extends PacketListener {

  private val _logger = LoggerFactory.getLogger(getClass)

  private var hooks: Seq[Hook] = Seq()

  def addHook(hook: Hook) = {
    hooks = hooks ++ Seq(hook)
  }

  def processPacket(packet: Packet) = {
    packet match {
      case message: Message =>
        hooks.foreach(hook => processHook(hook, message))
      case presence: Presence =>
        val mucUserOpt = Option(presence.getExtension("http://jabber.org/protocol/muc#user").asInstanceOf[MUCUser])

        mucUserOpt match {
          case Some(mucUser) =>
            val userJid = JID(mucUser.getItem.getJid)
            val userNick = JID(presence.getFrom).resource.get
            if (presence.isAvailable)
              rosterActor ! RosterJoin(StringUtils.parseBareAddress(presence.getFrom), userJid, userNick)
            else
              rosterActor ! RosterPart(StringUtils.parseBareAddress(presence.getFrom), userJid, userNick)

          case _ =>
            _logger.info("Ignoring non-MUC presence from %s.".format(presence.getFrom))
        }
      case _ =>
        _logger.info("Got unknown packet: %s".format(packet.toXML))
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
