/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

/**
 * @author roman
 *
 */
public class SmevData {

	private Header header;
	private Message message;
	private MessageData data;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public MessageData getData() {
		return data;
	}
	public void setData(MessageData data) {
		this.data = data;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		SmevData other = (SmevData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SmevData [header=" + header + ", message=" + message
				+ ", data=" + data + "]";
	}
	
}
