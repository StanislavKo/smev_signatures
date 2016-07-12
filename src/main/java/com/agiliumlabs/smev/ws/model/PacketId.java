/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

/**
 * @author roman
 *
 */
public class PacketId {

	private String messageId;
	private String subRequestNumber;
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSubRequestNumber() {
		return subRequestNumber;
	}
	public void setSubRequestNumber(String subRequestNumber) {
		this.subRequestNumber = subRequestNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		result = prime
				* result
				+ ((subRequestNumber == null) ? 0 : subRequestNumber.hashCode());
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
		PacketId other = (PacketId) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (subRequestNumber == null) {
			if (other.subRequestNumber != null)
				return false;
		} else if (!subRequestNumber.equals(other.subRequestNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PacketId [messageId=" + messageId + ", subRequestNumber="
				+ subRequestNumber + "]";
	}
	
}
