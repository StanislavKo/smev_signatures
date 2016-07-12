package com.agiliumlabs.smev.ws.ds.handlers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.agiliumlabs.smev.ws.ds.utils.XMLUtils;

public class LogHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("LogHandler : handleMessage() [" + (Boolean.TRUE.equals(isRequest) ? "REQUEST" : Boolean.FALSE.equals(isRequest) ? "RESPONSE" : "NULL") + "]...... ++++++++++++++++++++++++++++++++++++++++++++++++");

		try {
			SOAPMessage soapMsg = context.getMessage();
			SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnv.getHeader();

			System.out.println("Message");
			soapMsg.writeTo(System.out);
			System.out.println();
//			System.out.println("Header");
//			System.out.println(XMLUtils.nodeToString(soapHeader));

		} catch (SOAPException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("LogHandler : handleFault()......");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("LogHandler : close()......");
	}

	public Set<QName> getHeaders() {
		System.out.println("LogHandler : getHeaders()......");
		return null;
	}

}
