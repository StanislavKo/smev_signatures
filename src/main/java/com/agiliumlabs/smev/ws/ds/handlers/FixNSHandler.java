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

public class FixNSHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("FixNSHandler : handleMessage() [" + (Boolean.TRUE.equals(isRequest) ? "REQUEST" : Boolean.FALSE.equals(isRequest) ? "RESPONSE" : "NULL") + "]...... ++++++++++++++++++++++++++++++++++++++++++++++++");

		if (Boolean.TRUE.equals(isRequest)) {
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();

//				soapEnv.setPrefix("soap");
//				soapMsg.getSOAPBody().setPrefix("soap");
				soapEnv.addNamespaceDeclaration("ds", "http://www.w3.org/2000/09/xmldsig#");
				soapEnv.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelope/");
				soapEnv.addNamespaceDeclaration("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
				soapEnv.addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

//				System.out.println("Message");
//				soapMsg.writeTo(System.out);
//				System.out.println();

			} catch (SOAPException e) {
				System.err.println(e);
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("FixNSHandler : handleFault()......");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("FixNSHandler : close()......");
	}

	public Set<QName> getHeaders() {
		System.out.println("FixNSHandler : getHeaders()......");
		return null;
	}

}
