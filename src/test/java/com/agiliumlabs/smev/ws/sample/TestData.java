/**
 * 
 */
package com.agiliumlabs.smev.ws.sample;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author roman
 *
 */
@XmlRootElement
public class TestData {

	private String data;

	public TestData(String data) {
		super();
		this.data = data;
	}

	public TestData() {
		super();
		// TODO Auto-generated constructor stub
	}

	@XmlValue
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TestData other = (TestData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TestData [data=" + data + "]";
	}
	
}
