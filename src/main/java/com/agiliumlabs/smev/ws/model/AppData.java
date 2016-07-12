/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * @author roman
 *
 */
public class AppData {

	private List<Object> data;
	private Map<QName, String> attributes;
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data != null ? new ArrayList<Object>(data) : null;
	}
	public Map<QName, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<QName, String> attributes) {
		this.attributes = attributes != null ? new HashMap<QName, String>(attributes) : null;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		AppData other = (AppData) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AppData [data=" + data + ", attributes=" + attributes + "]";
	}
	
}
