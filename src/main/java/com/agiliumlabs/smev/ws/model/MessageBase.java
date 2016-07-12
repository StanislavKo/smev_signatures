/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

import java.util.Date;

/**
 * @author roman
 *
 */
public abstract class MessageBase {

	private Organization originator;
	private Status status;
	private Date date;
	private String requestIdRef;
	private String originRequestIdRef;
	private String serviceCode;
	private String caseNumber;
	public Organization getOriginator() {
		return originator;
	}
	public void setOriginator(Organization originator) {
		this.originator = originator;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRequestIdRef() {
		return requestIdRef;
	}
	public void setRequestIdRef(String requestIdRef) {
		this.requestIdRef = requestIdRef;
	}
	public String getOriginRequestIdRef() {
		return originRequestIdRef;
	}
	public void setOriginRequestIdRef(String originRequestIdRef) {
		this.originRequestIdRef = originRequestIdRef;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((caseNumber == null) ? 0 : caseNumber.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime
				* result
				+ ((originRequestIdRef == null) ? 0 : originRequestIdRef
						.hashCode());
		result = prime * result
				+ ((originator == null) ? 0 : originator.hashCode());
		result = prime * result
				+ ((requestIdRef == null) ? 0 : requestIdRef.hashCode());
		result = prime * result
				+ ((serviceCode == null) ? 0 : serviceCode.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		MessageBase other = (MessageBase) obj;
		if (caseNumber == null) {
			if (other.caseNumber != null)
				return false;
		} else if (!caseNumber.equals(other.caseNumber))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (originRequestIdRef == null) {
			if (other.originRequestIdRef != null)
				return false;
		} else if (!originRequestIdRef.equals(other.originRequestIdRef))
			return false;
		if (originator == null) {
			if (other.originator != null)
				return false;
		} else if (!originator.equals(other.originator))
			return false;
		if (requestIdRef == null) {
			if (other.requestIdRef != null)
				return false;
		} else if (!requestIdRef.equals(other.requestIdRef))
			return false;
		if (serviceCode == null) {
			if (other.serviceCode != null)
				return false;
		} else if (!serviceCode.equals(other.serviceCode))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "originator=" + originator + ", status=" + status
				+ ", date=" + date + ", requestIdRef=" + requestIdRef
				+ ", originRequestIdRef=" + originRequestIdRef
				+ ", serviceCode=" + serviceCode + ", caseNumber=" + caseNumber;
	}
	
}
