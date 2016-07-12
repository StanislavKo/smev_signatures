package com.agiliumlabs.smev.ws.ds.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Set;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.ws.security.WSSecurityException;
import org.apache.xml.security.transforms.TransformationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.agiliumlabs.smev.ws.ds.utils.SignatureUtils;
import com.agiliumlabs.smev.ws.ds.utils.XMLUtils;

public class SignAppDataHandler implements SOAPHandler<SOAPMessageContext> {

	private PrivateKey privateKey;
	private X509Certificate cert;

	public SignAppDataHandler(PrivateKey privateKey, X509Certificate cert) {
		this.privateKey = privateKey;
		this.cert = cert;
	}
	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("SignAppDataHandler : handleMessage() [" + (Boolean.TRUE.equals(isRequest) ? "REQUEST" : Boolean.FALSE.equals(isRequest) ? "RESPONSE" : "NULL") + "]...... ++++++++++++++++++++++++++++++++++++++++++++++++");

		if (Boolean.TRUE.equals(isRequest)) {
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();
	
				((Element) XMLUtils.getElement(soapMsg.getSOAPBody(), "//smev:MessageData/smev:AppData")).setAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu:Id", "appDataBody");
				
				SignatureUtils.signAppDataSignature(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument(), "#appDataBody", privateKey, cert);
			} catch (SOAPException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			} catch (TransformationException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			} catch (DOMException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (MarshalException e) {
				e.printStackTrace();
			} catch (XMLSignatureException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("SignAppDataHandler : handleFault()......");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("SignAppDataHandler : close()......");
	}

	public Set<QName> getHeaders() {
		System.out.println("SignAppDataHandler : getHeaders()......");
		return null;
	}

}
