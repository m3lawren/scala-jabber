package com.z0rchain.jabber

import org.jivesoftware.smack.util.StringUtils

case class JID(domain: String, node: Option[String], resource: Option[String]) {
  override def toString = {
    node.map(_ + "@").getOrElse("") + domain + resource.map("/" + _).getOrElse("")
  }
}

object JID {
  def apply(jid: String): JID = {
    val domain = StringUtils.parseServer(jid)
    val node = StringUtils.parseName(jid)
    val resource = StringUtils.parseResource(jid)

    JID(domain, Option(node).filterNot(_.trim.isEmpty), Option(resource).filterNot(_.trim.isEmpty))
  }
}
