/**
 * 
 */
package com.agiliumlabs.smev.ws.model;

import java.util.List;

/**
 * @author roman
 *
 */
public class Message extends MessageBase {

	private Organization sender;
	private Organization recepient;
	private String serviceName;
	private Service service;
	private TypeCode type;
	private String exchangeType;
	private List<SubMessage> subMessages;
	private String test;
	private String oktmo;
	public Organization getSender() {
		return sender;
	}
	public void setSender(Organization sender) {
		this.sender = sender;
	}
	public Organization getRecepient() {
		return recepient;
	}
	public void setRecepient(Organization recepient) {
		this.recepient = recepient;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public TypeCode getType() {
		return type;
	}
	public void setType(TypeCode type) {
		this.type = type;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public List<SubMessage> getSubMessages() {
		return subMessages;
	}
	public void setSubMessages(List<SubMessage> subMessages) {
		this.subMessages = subMessages;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getOktmo() {
		return oktmo;
	}
	public void setOktmo(String oktmo) {
		this.oktmo = oktmo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((exchangeType == null) ? 0 : exchangeType.hashCode());
		result = prime * result + ((oktmo == null) ? 0 : oktmo.hashCode());
		result = prime * result
				+ ((recepient == null) ? 0 : recepient.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result
				+ ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime * result
				+ ((subMessages == null) ? 0 : subMessages.hashCode());
		result = prime * result + ((test == null) ? 0 : test.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Message other = (Message) obj;
		if (exchangeType == null) {
			if (other.exchangeType != null)
				return false;
		} else if (!exchangeType.equals(other.exchangeType))
			return false;
		if (oktmo == null) {
			if (other.oktmo != null)
				return false;
		} else if (!oktmo.equals(other.oktmo))
			return false;
		if (recepient == null) {
			if (other.recepient != null)
				return false;
		} else if (!recepient.equals(other.recepient))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		if (subMessages == null) {
			if (other.subMessages != null)
				return false;
		} else if (!subMessages.equals(other.subMessages))
			return false;
		if (test == null) {
			if (other.test != null)
				return false;
		} else if (!test.equals(other.test))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Message [sender=" + sender + ", recepient=" + recepient
				+ ", serviceName=" + serviceName + ", service=" + service
				+ ", type=" + type + ", exchangeType=" + exchangeType
				+ ", subMessages=" + subMessages + ", test=" + test
				+ ", oktmo=" + oktmo + ", " + super.toString() + "]";
	}
	
}
