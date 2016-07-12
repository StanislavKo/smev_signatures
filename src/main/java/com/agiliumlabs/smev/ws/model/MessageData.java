package com.agiliumlabs.smev.ws.model;

/**
 * @author roman
 *
 */
public class MessageData {
	
	private AppData data;
	private AppDocument document;
	public AppData getData() {
		return data;
	}
	public void setData(AppData data) {
		this.data = data;
	}
	public AppDocument getDocument() {
		return document;
	}
	public void setDocument(AppDocument document) {
		this.document = document;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
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
		MessageData other = (MessageData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MessageData [data=" + data + ", document=" + document + "]";
	}

}
