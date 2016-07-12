/**
 * 
 */
package com.agiliumlabs.smev.ws.impl.client.example;

/**
 * @author roman
 *
 */
public class ExampleClientConfiguration {
	
	private String wsdlUrl = "http://188.254.16.92:7777/gateway/services/SID0003022?wsdl";
	public String getWsdlUrl() {
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((wsdlUrl == null) ? 0 : wsdlUrl.hashCode());
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
		ExampleClientConfiguration other = (ExampleClientConfiguration) obj;
		if (wsdlUrl == null) {
			if (other.wsdlUrl != null)
				return false;
		} else if (!wsdlUrl.equals(other.wsdlUrl))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ExampleClientConfiguration [wsdlUrl=" + wsdlUrl + "]";
	}

}
