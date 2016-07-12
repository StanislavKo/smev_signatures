/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

/**
 * @author roman
 *
 */
public class SubMessage extends MessageBase {

    protected String subRequestNumber;
	public String getSubRequestNumber() {
		return subRequestNumber;
	}
	public void setSubRequestNumber(String subRequestNumber) {
		this.subRequestNumber = subRequestNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((subRequestNumber == null) ? 0 : subRequestNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubMessage other = (SubMessage) obj;
		if (subRequestNumber == null) {
			if (other.subRequestNumber != null)
				return false;
		} else if (!subRequestNumber.equals(other.subRequestNumber))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SubMessage [subRequestNumber=" + subRequestNumber + ", " + super.toString() + "]";
	}

}
