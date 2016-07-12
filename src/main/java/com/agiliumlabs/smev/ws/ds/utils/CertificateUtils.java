package com.agiliumlabs.smev.ws.ds.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.Base64;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CertificateUtils {

	private static final String WS3318_ENDPOINT = "http://195.245.214.33:7777/esv/SignatureTool.asmx";
	private static final String WS3318_SOAPACTION = "http://esv.server.rt.ru/VerifyCertificateWithReport";
	private static final String WS3318_NAMESPACE_URI = "http://esv.server.rt.ru";
	
	public static Certificate3318Info verifyCertificate(String certBase64) {
		System.out.println("CertificateUtils.verifyCertificate()");
		Certificate3318Info cert3318 = invokeVerifyCertificateWithReport(certBase64);
		if (cert3318 == null || cert3318.getCode() != 0) {
			System.err.println("Certificate is not valid " + (cert3318 != null ? "[" + cert3318.getDescription() + "]" : ""));
		}
		return cert3318;
	}
	
	private static Certificate3318Info invokeVerifyCertificateWithReport(String certBase64) {
		javax.xml.soap.SOAPConnection connection = null;
		try {
			javax.xml.soap.SOAPConnectionFactory factory = javax.xml.soap.SOAPConnectionFactory.newInstance();
			connection = factory.createConnection();
			java.net.URL endpoint = new java.net.URL(WS3318_ENDPOINT);

			javax.xml.soap.MessageFactory mFactory = javax.xml.soap.MessageFactory.newInstance(javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL);
			javax.xml.soap.SOAPMessage request = mFactory.createMessage();
			javax.xml.soap.SOAPHeader header = request.getSOAPHeader();
			javax.xml.soap.MimeHeaders mimeHeader = request.getMimeHeaders();
			mimeHeader.setHeader("SOAPAction", WS3318_SOAPACTION);
			header.detachNode();
			javax.xml.soap.SOAPBody body = request.getSOAPBody();
			javax.xml.namespace.QName meth = new javax.xml.namespace.QName(WS3318_NAMESPACE_URI, "VerifyCertificateWithReport");
			javax.xml.soap.SOAPBodyElement methElement = body.addBodyElement(meth);
			javax.xml.namespace.QName val1 = new javax.xml.namespace.QName(WS3318_NAMESPACE_URI, "certificate");
			javax.xml.soap.SOAPElement val1Element = methElement.addChildElement(val1);
			val1Element.setTextContent(certBase64);

//			request.writeTo(System.out);
			javax.xml.soap.SOAPMessage response = connection.call(request, endpoint);
			
			System.out.println("responseBody:" + response.getSOAPBody().getTextContent());
			Integer code = Integer.valueOf(XMLUtils.getElementValue(response.getSOAPBody(), "ws3318:VerifyCertificateWithReportResponse/ws3318:VerifyCertificateWithReportResult/ws3318:Code"));
			String description = XMLUtils.getElementValue(response.getSOAPBody(), "ws3318:VerifyCertificateWithReportResponse/ws3318:VerifyCertificateWithReportResult/ws3318:Description");
			String reportBase64Str = XMLUtils.getElementValue(response.getSOAPBody(), "ws3318:VerifyCertificateWithReportResponse/ws3318:VerifyCertificateWithReportResult/ws3318:Report");
			System.out.println("  code:" + code);
			System.out.println("  description:" + description);
			Certificate3318Info cert3318 = new Certificate3318Info(code, description, reportBase64Str);
			String reportStr = new String(Base64.decode(reportBase64Str));
			
			Document doc = XMLUtils.readXMLString(reportStr);
//			System.out.println("asdf:" + XMLUtils.getElementValue(doc.getDocumentElement(), "UsedCerts"));
			cert3318.setReportDate(doc.getDocumentElement().getAttributes().getNamedItem("ReportDate").getTextContent());
			cert3318.setInputDataHash(doc.getDocumentElement().getAttributes().getNamedItem("InputDataHash").getTextContent());
			cert3318.setResultText(doc.getDocumentElement().getAttributes().getNamedItem("ResultText").getTextContent());
			cert3318.setCert1Subject(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("Subject").getTextContent());
			cert3318.setCert1Serial(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("Serial").getTextContent());
			cert3318.setCert1Issuer(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("Issuer").getTextContent());
			cert3318.setCert1Thumbprint(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("Thumbprint").getTextContent());
			cert3318.setCert1NotAfter(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("NotAfter").getTextContent());
			cert3318.setCert1NotBefore(XMLUtils.getElement(doc.getDocumentElement(), "UsedCerts/Cert").getAttributes().getNamedItem("NotBefore").getTextContent());
			
			return cert3318;
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WSSecurityException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static class Certificate3318Info {
		private Integer code;
		private String description;
		private String report;
		private String reportDate;
		private String resultText;
		private String inputDataHash;
		private String cert1Subject;
		private String cert1Serial;
		private String cert1Issuer;
		private String cert1Thumbprint;
		private String cert1NotBefore;
		private String cert1NotAfter;
		public Certificate3318Info(Integer code, String description, String report) {
			super();
			this.code = code;
			this.description = description;
			this.report = report;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getReport() {
			return report;
		}
		public void setReport(String report) {
			this.report = report;
		}
		public String getReportDate() {
			return reportDate;
		}
		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}
		public String getResultText() {
			return resultText;
		}
		public void setResultText(String resultText) {
			this.resultText = resultText;
		}
		public String getInputDataHash() {
			return inputDataHash;
		}
		public void setInputDataHash(String inputDataHash) {
			this.inputDataHash = inputDataHash;
		}
		public String getCert1Subject() {
			return cert1Subject;
		}
		public void setCert1Subject(String cert1Subject) {
			this.cert1Subject = cert1Subject;
		}
		public String getCert1Serial() {
			return cert1Serial;
		}
		public void setCert1Serial(String cert1Serial) {
			this.cert1Serial = cert1Serial;
		}
		public String getCert1Issuer() {
			return cert1Issuer;
		}
		public void setCert1Issuer(String cert1Issuer) {
			this.cert1Issuer = cert1Issuer;
		}
		public String getCert1Thumbprint() {
			return cert1Thumbprint;
		}
		public void setCert1Thumbprint(String cert1Thumbprint) {
			this.cert1Thumbprint = cert1Thumbprint;
		}
		public String getCert1NotBefore() {
			return cert1NotBefore;
		}
		public void setCert1NotBefore(String cert1NotBefore) {
			this.cert1NotBefore = cert1NotBefore;
		}
		public String getCert1NotAfter() {
			return cert1NotAfter;
		}
		public void setCert1NotAfter(String cert1NotAfter) {
			this.cert1NotAfter = cert1NotAfter;
		}
	}
	
	public static class CertificateInfo {
		X509Certificate cert;
		String certBase64;
		public CertificateInfo(X509Certificate cert, String certBase64) {
			super();
			this.cert = cert;
			this.certBase64 = certBase64;
		}
		public X509Certificate getCert() {
			return cert;
		}
		public void setCert(X509Certificate cert) {
			this.cert = cert;
		}
		public String getCertBase64() {
			return certBase64;
		}
		public void setCertBase64(String certBase64) {
			this.certBase64 = certBase64;
		}
	}
	
}
