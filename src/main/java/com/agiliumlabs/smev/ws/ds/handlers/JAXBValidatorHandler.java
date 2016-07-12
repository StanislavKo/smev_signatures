package com.agiliumlabs.smev.ws.ds.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.agiliumlabs.smev.ws.ds.Consts;
import com.agiliumlabs.smev.ws.ds.utils.XMLUtils;

public class JAXBValidatorHandler implements SOAPHandler<SOAPMessageContext> {

	private Schema schema;
	private Validator validator;
	
	public JAXBValidatorHandler() {
		//Setting the Validation
//		try {
//			System.out.println("WebServiceContext: " + context);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		try {
//			System.out.println(this.getClass().getClassLoader().getResourceAsStream("/resources/xsd/include.xsd"));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		try {
//			System.out.println(this.getClass().getClassLoader().getResourceAsStream("WEB-INF/web.xml"));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		try {
//			System.out.println(this.getClass().getClassLoader().getResourceAsStream("index.html"));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		try {
//			System.out.println(this.getClass().getClassLoader().getResourceAsStream("index.html"));
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		javax.xml.transform.Source xsd1 = new javax.xml.transform.stream.StreamSource(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("/resources/xsd/include.xsd")));
//		javax.xml.transform.Source xsd2 = new javax.xml.transform.stream.StreamSource(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("/resources/xsd/rrgts_appdata.xsd")));
//		javax.xml.transform.Source xsd3 = new javax.xml.transform.stream.StreamSource(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("/resources/xsd/smev.gosuslugi.ru.rev120315.xsd")));
//		javax.xml.transform.Source xsd4 = new javax.xml.transform.stream.StreamSource(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("/resources/xsd/rrgtsspu.xsd")));
//		javax.xml.transform.Source[] xsds = new javax.xml.transform.Source[] {xsd1, xsd2, xsd3, xsd4}; 

	}

	private synchronized void initHandler() {
//		if (validator == null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );//W3C_XML_SCHEMA_NS_URI
			try {
				javax.xml.transform.Source xsd1 = new javax.xml.transform.stream.StreamSource("http://www.xmlsoap.org/soap/envelope/");
//				javax.xml.transform.Source xsd2 = new javax.xml.transform.stream.StreamSource("http://localhost:8080/SMEV_08-HydroXSD-Mvn-0.0.1-SNAPSHOT/wsdl/smev.gosuslugi.ru.rev120315.xsd");
				javax.xml.transform.Source xsd2 = new javax.xml.transform.stream.StreamSource(new File(Consts.FILE_WSDL_SMEV));
				javax.xml.transform.Source[] xsds = new javax.xml.transform.Source[] {xsd1, xsd2}; 
				
				schema = schemaFactory.newSchema(xsds);
				validator = schema.newValidator();
				validator.setErrorHandler(new ErrorHandler() {
					private Pattern pattern = Pattern.compile("Invalid\\scontent\\swas\\sfound\\sstarting\\swith\\selement\\s'[\\w]+:Reference'\\.\\sNo\\schild\\selement\\sis\\sexpected\\sat\\sthis\\spoint");
					public void warning(SAXParseException exception) throws SAXException {
						System.out.println("WARNING:" + exception.toString());
					}
					public void fatalError(SAXParseException exception) throws SAXException {
						throw exception;
					}
					public void error(SAXParseException ex) throws SAXException {
						String text = ex.getMessage();
						if (pattern.matcher(text).find()) {
							System.out.println("Skipping validation for XSD element <MTOM>.");
							return;
						}
						throw ex;
					}
				});
			} catch (SAXException e) { 
				e.printStackTrace();
			}
//		}
	}
	
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		System.out.println("JAXBValidatorHandler : handleMessage() [" + (Boolean.TRUE.equals(isRequest) ? "REQUEST" : Boolean.FALSE.equals(isRequest) ? "RESPONSE" : "NULL") + "]...... ++++++++++++++++++++++++++++++++++++++++++++++++");
		initHandler();

		if (Boolean.FALSE.equals(isRequest)) {
			try {
				SOAPMessage soapMsg = context.getMessage();
				SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnv.getHeader();

//				String msgStr = XMLUtils.nodeToString(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument().getDocumentElement());
				DOMSource source = new DOMSource(soapMsg.getSOAPPart().getEnvelope().getOwnerDocument());
				DOMResult result = new DOMResult();
				validator.validate(source, result);
//				validator.validate(new javax.xml.transform.stream.StreamSource(new StringReader(msgStr)), result);
				Document augmented = (Document) result.getNode();
				System.out.println("SOAP message is valid.");
//				String msgStr = XMLUtils.nodeToString(augmented.getDocumentElement());
//				System.out.println("msgStr:" + msgStr);
	
			} catch (SOAPException e) {
				System.err.println(e);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("JAXBValidatorHandler : handleFault()......");
		return true;
	}

	public void close(MessageContext context) {
		System.out.println("JAXBValidatorHandler : close()......");
	}

	public Set<QName> getHeaders() {
		System.out.println("JAXBValidatorHandler : getHeaders()......");
		return null;
	}

}
