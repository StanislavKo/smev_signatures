/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * @author roman
 *
 */
public class Header {
	
	private String nodeId;
	private String messageId;
	private Date timestamp;
	private MessageClass messageClass;
	private List<PacketId> packetIds;
	private String actor;
	private Map<QName, String> attributes;
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public MessageClass getMessageClass() {
		return messageClass;
	}
	public void setMessageClass(MessageClass messageClass) {
		this.messageClass = messageClass;
	}
	public List<PacketId> getPacketIds() {
		return packetIds;
	}
	public void setPacketIds(List<PacketId> packetIds) {
		this.packetIds = packetIds;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public Map<QName, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<QName, String> attributes) {
		this.attributes = attributes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((messageClass == null) ? 0 : messageClass.hashCode());
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result
				+ ((packetIds == null) ? 0 : packetIds.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Header other = (Header) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (messageClass != other.messageClass)
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (packetIds == null) {
			if (other.packetIds != null)
				return false;
		} else if (!packetIds.equals(other.packetIds))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Header [nodeId=" + nodeId + ", messageId=" + messageId
				+ ", timestamp=" + timestamp + ", messageClass=" + messageClass
				+ ", packetIds=" + packetIds + ", actor=" + actor
				+ ", attributes=" + attributes + "]";
	}

}
