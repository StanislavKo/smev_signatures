package com.agiliumlabs.smev.ws.ds;

import java.lang.reflect.InvocationTargetException;
import java.security.Provider;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import ru.CryptoPro.JCPxml.xmldsig.JCPXMLDSigInit;

public class Infrastructure {

	private static Boolean isInited = false;
	private static Provider xmlDSigProvider;
	private static XMLSignatureFactory signatureFactory;
	private static MessageFactory messageFactory;
	private static DocumentBuilderFactory documentBuilderFactory;
	private static DocumentBuilder documentBuilder;

	public synchronized static void init() throws SOAPException, ParserConfigurationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		if (!isInited) {
			// Инициализация Transforms алгоритмов.
			Class.forName("com.sun.org.apache.xml.internal.security.Init").getMethod("init").invoke(null);
//			com.sun.org.apache.xml.internal.security.Init.init();

			// Инициализация JCP XML провайдера.
			if (!JCPXMLDSigInit.isInitialized()) {
				JCPXMLDSigInit.init();
			}
			
			xmlDSigProvider = new ru.CryptoPro.JCPxml.dsig.internal.dom.XMLDSigRI();
			signatureFactory = XMLSignatureFactory.getInstance("DOM", xmlDSigProvider);
			messageFactory = MessageFactory.newInstance();
			
			// common factories
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			documentBuilderFactory. setExpandEntityReferences(true);
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			isInited = true; 
		}
	}
	
	public synchronized static void destroy() {
	}

	public static Provider getJCPProvider() {
		return xmlDSigProvider;
	}

	public static XMLSignatureFactory getXMLSignatureFactory() {
		return signatureFactory;
	}

	public static MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		return documentBuilderFactory;
	}

	public static DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

}
