/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

import java.util.Arrays;

import javax.activation.DataHandler;

import org.apache.commons.codec.binary.Hex;

/**
 * @author roman
 *
 */
public class AppDocument {

	private String requestCode;
	private byte[] binaryData;
	private byte[] digest;
	private DataHandler reference;
	public String getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}
	public byte[] getBinaryData() {
		return binaryData;
	}
	public void setBinaryData(byte[] binaryData) {
		this.binaryData = binaryData;
	}
	public byte[] getDigest() {
		return digest;
	}
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}
	public DataHandler getReference() {
		return reference;
	}
	public void setReference(DataHandler reference) {
		this.reference = reference;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(binaryData);
		result = prime * result + Arrays.hashCode(digest);
		result = prime * result
				+ ((requestCode == null) ? 0 : requestCode.hashCode());
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
		AppDocument other = (AppDocument) obj;
		if (!Arrays.equals(binaryData, other.binaryData))
			return false;
		if (!Arrays.equals(digest, other.digest))
			return false;
		if (requestCode == null) {
			if (other.requestCode != null)
				return false;
		} else if (!requestCode.equals(other.requestCode))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AppDocument [requestCode=" + requestCode + ", binaryData.length="
				+ (binaryData != null ? binaryData.length : null) + ", digest="
				+ Hex.encodeHexString(digest) + "]";
	}
	
}
