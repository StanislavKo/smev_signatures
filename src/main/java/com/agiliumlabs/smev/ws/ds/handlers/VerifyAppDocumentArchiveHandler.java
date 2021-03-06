package com.agiliumlabs.smev.ws.ds.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;
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

import com.agiliumlabs.smev.ws.ds.Consts;
import com.agiliumlabs.smev.ws.ds.Infrastructure;
import com.agiliumlabs.smev.ws.ds.utils.PKCS7Utils;
import com.agiliumlabs.smev.ws.ds.utils.SignatureUtils;
import com.agiliumlabs.smev.ws.ds.utils.XMLUtils;
import com.agiliumlabs.smev.ws.ds.utils.CertificateUtils.CertificateInfo;

public class VerifyAppDocumentArchiveHandler implements SOAPHandler<SOAPMessageContext> {

	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("VerifyAppDocumentArchiveHandler : handleMessage() [" + (Boolean.TRUE.equals(isRequest) ? "REQUEST" : Boolean.FALSE.equals(isRequest) ? "RESPONSE" : "NULL") + "]...... ++++++++++++++++++++++++++++++++++++++++++++++++");

		if (Boolean.FALSE.equals(isRequest)) {
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();
	
				if (XMLUtils.getElements(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument().getDocumentElement(), "//smev:MessageData/smev:AppDocument").getLength() != 0) {
					Infrastructure.init();
					Set<CertificateInfo> certs = SignatureUtils.verifyHeaderSignature(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument());
					PKCS7Utils.verifyInsideArchives(certs, soapMsg.getSOAPPart().getEnvelope().getOwnerDocument());
				} else {
					System.out.println("There is no smev:AppDocument blocks, nothing to verify");
				}
				
	//			System.out.println("Message");
	//			soapMsg.writeTo(System.out);
	//			System.out.println();
	//			System.out.println("Header");
	//			System.out.println(XMLUtils.nodeToString(soapHeader));
			} catch (SOAPException e) {
				System.err.println(e);
			} catch (TransformerException e) {
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
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WSSecurityException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("VerifyAppDocumentArchiveHandler : handleFault()......");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("VerifyAppDocumentArchiveHandler : close()......");
	}

	public Set<QName> getHeaders() {
		System.out.println("VerifyAppDocumentArchiveHandler : getHeaders()......");
		return null;
	}

}
