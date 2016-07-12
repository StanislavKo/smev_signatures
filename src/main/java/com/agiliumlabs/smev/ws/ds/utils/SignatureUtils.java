package com.agiliumlabs.smev.ws.ds.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.token.X509Security;
import org.apache.ws.security.util.Base64;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.agiliumlabs.smev.ws.ds.Consts;
import com.agiliumlabs.smev.ws.ds.Infrastructure;
import com.agiliumlabs.smev.ws.ds.utils.CertificateUtils.CertificateInfo;

public class SignatureUtils {

	public static void signHeaderSignature(Document doc, String refId, PrivateKey privateKey, X509Certificate cert) throws SOAPException, WSSecurityException, TransformationException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException, DOMException, TransformerException, MarshalException, XMLSignatureException, ParserConfigurationException, SAXException,
			IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Infrastructure.init();

		// Добавляем xmldsig# блок в header (wsse:Security)
		Document dsSignatureDoc = XMLUtils.readXMLString(FileUtils.readUTF8File(Consts.FILE_SIGNATURE_DSIG_TEMPLATE));
		Element dsSignatureRootEl = dsSignatureDoc.getDocumentElement();
//		System.out.println("asdf:" + dsSignatureDoc.getDocumentElement());
		String dsSignatureCertId = UUID.randomUUID().toString();
		Element dsSignatureBinaryToken = (Element) XMLUtils.getElement(dsSignatureRootEl, "wsse:BinarySecurityToken[@wsu:Id='']");
		dsSignatureBinaryToken.setAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu:Id", dsSignatureCertId);
		Node dsSignatureRootImportedEl = doc.importNode(dsSignatureRootEl, true);
		XMLUtils.getElement(doc.getDocumentElement(), "soap:Header").appendChild(dsSignatureRootImportedEl);

		// Формируем заголовок.
		WSSecHeader header = new WSSecHeader();
		header.setActor("http://smev.gosuslugi.ru/actors/smev");
		header.setMustUnderstand(false);

		// Получаем документ.
//		Document doc = message.getSOAPPart().getEnvelope().getOwnerDocument();
		header.insertSecurityHeader(doc);

		// Подписываемый элемент.
		Element token = header.getSecurityHeader();

		/*** Подпись данных ***/
		KeySignPair keySignPair = signXMLDSIGImpl(XMLDSIGScope.HEADER, doc, token, refId, privateKey, cert);

		// Элемент SenderCertificate, который должен содержать сертификат.
		Element cerVal = (Element) XPathAPI.selectSingleNode(token, "//*[@wsu:Id='" + dsSignatureCertId + "']");
		cerVal.setTextContent(XPathAPI.selectSingleNode(keySignPair.keyE, "//ds:X509Certificate", keySignPair.keyE).getFirstChild().getNodeValue());

		// Удаляем элементы KeyInfo, попавшие в тело документа. Они должны быть только в header.
		keySignPair.keyE.removeChild(XPathAPI.selectSingleNode(keySignPair.keyE, "//ds:X509Data", keySignPair.keyE));

		NodeList chl = keySignPair.keyE.getChildNodes();

		for (int i = 0; i < chl.getLength(); i++) {
			keySignPair.keyE.removeChild(chl.item(i));
		}

		// Блок KeyInfo содержит указание на проверку подписи с помощью сертификата SenderCertificate.
		Node str = keySignPair.keyE.appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:SecurityTokenReference"));
		Element strRef = (Element) str.appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:Reference"));

		strRef.setAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
		strRef.setAttribute("URI", "#" + dsSignatureCertId);
		header.getSecurityHeader().appendChild(keySignPair.sigE);
	}

	public static void signAppDataSignature(Document doc, String refId, PrivateKey privateKey, X509Certificate cert) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			TransformationException, TransformerException, MarshalException, XMLSignatureException, ParserConfigurationException, SAXException, IOException, SOAPException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Infrastructure.init();

		Element appDataEl = (Element) XMLUtils.getElement(doc.getDocumentElement(), "//smev:MessageData/smev:AppData[@wsu:Id]");
		Element messageDataEl = (Element) appDataEl.getParentNode();

		/*** Подпись данных ***/
		KeySignPair keySignPair = signXMLDSIGImpl(XMLDSIGScope.APP_DATA, doc, messageDataEl, refId, privateKey, cert);

		appDataEl.appendChild(keySignPair.sigE);
	}

	public static void generateAppDocumentInner(Document doc, String refId, String archiveFolder, String archiveRequestCode, PrivateKey privateKey, X509Certificate cert) throws NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, TransformationException, TransformerException, MarshalException, XMLSignatureException, ParserConfigurationException, SAXException, IOException,
			SOAPException, InvalidKeyException, IllegalArgumentException, SignatureException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		Infrastructure.init();

		// ********************* AppDocument *************************

		List<String> files = new ArrayList<String>();
		Boolean isRequestXmlExists = false;
		String tempFolderPath = new File(archiveFolder).getParent() + File.separator + new File(archiveFolder).getName() + UUID.randomUUID().toString();
		String tempArchivePath = archiveFolder + UUID.randomUUID().toString() + ".zip";
		new File(tempFolderPath).mkdir();
		
		File folder = new File(archiveFolder);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (("req_" + archiveRequestCode + ".xml").equals(listOfFiles[i].getName())) {
					isRequestXmlExists = true;
				}
				String filename = listOfFiles[i].getAbsolutePath();
				System.out.println("File " + filename);

				byte[] requestBytes = FileUtils.readFile(filename);
				byte[] requestPkcs7bytes = PKCS7Utils.createPKCS7(privateKey, cert, requestBytes);
				FileUtils.writeFile(tempFolderPath + File.separator + listOfFiles[i].getName() + ".sig", requestPkcs7bytes);
				PKCS7Utils.verifyPKCS7(cert, requestPkcs7bytes, requestBytes);
				
				files.add(filename);
				files.add(tempFolderPath + File.separator + listOfFiles[i].getName() + ".sig");
			}
		}
		if (!isRequestXmlExists) {
			throw new IllegalArgumentException("Illegal archive structure");
		}
		
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempArchivePath)));
		for (String file : files) {
			FileUtils.addFile2Zip(out, file);
		}
		IOUtils.closeQuietly(out);
		
		String archiveBase64 = Base64.encode(FileUtils.readFile(tempArchivePath));
		FileUtils.deleteFolder(tempFolderPath);
		new File(tempArchivePath).delete();

//		Document appDocumentInner = XMLUtils.readXMLString(FileUtils.readUTF8File(Consts.FILE_APP_DOCUMENT_INNER_TEMPLATE));
//		Element appDocumentInnerRootEl = appDocumentInner.getDocumentElement();
//		Element requestCodeEl = (Element) XMLUtils.getElement(appDocumentInnerRootEl, "smev:AppDocument/smev:RequestCode");
//		Element binaryEl = (Element) XMLUtils.getElement(appDocumentInnerRootEl, "smev:AppDocument/smev:BinaryData");
//		requestCodeEl.setTextContent("req_" + archiveRequestCode);
//		binaryEl.setTextContent(archiveBase64);
//		Node appDocumentInnerRootImportedEl = doc.importNode(appDocumentInnerRootEl, true);
//		XMLUtils.getElement(doc.getDocumentElement(), "//*[@Id='" + refId + "']").getParentNode().appendChild(appDocumentInnerRootImportedEl);
		XMLUtils.getElement(doc.getDocumentElement(), "//smev:MessageData/smev:AppDocument[@Id='" + refId + "']/smev:RequestCode").setTextContent("req_" + archiveRequestCode);
		XMLUtils.getElement(doc.getDocumentElement(), "//smev:MessageData/smev:AppDocument[@Id='" + refId + "']/smev:BinaryData").setTextContent(archiveBase64);
		((Element) XMLUtils.getElement(doc.getDocumentElement(), "//smev:MessageData/smev:AppDocument[@Id='" + refId + "']")).removeAttribute("Id");
	}

	public static Set<CertificateInfo> verifyHeaderSignature(Document doc) throws TransformerException {
		System.out.println("SignatureUtils.verifyHeaderSignature()");
		NodeList nl = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
		if (nl.getLength() == 0) {
			throw new IllegalArgumentException("Не найден элемент Signature.");
		}

		NodeList elIdElems = XMLUtils.getElements(doc.getDocumentElement(), "//*[@Id or @wsu:Id]");
		for (int i = 0; i < elIdElems.getLength(); i++) {
			Node idElemCur = elIdElems.item(i);
			Element bodyElement = (Element) idElemCur;
			System.out.println("   " + idElemCur.getNodeName());
			Attr idAttr = bodyElement.getAttributeNodeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");
			if (idAttr != null) {
				bodyElement.setIdAttributeNode(idAttr, true);
			} else {
				idAttr = bodyElement.getAttributeNodeNS(null, "Id");
				if (idAttr != null) {
					bodyElement.setIdAttributeNode(idAttr, true);
				}
			}
		}

		XMLSignatureFactory fac = Infrastructure.getXMLSignatureFactory();

		Set<CertificateInfo> certs = new HashSet<CertificateInfo>();

		NodeList elHeaderSignatures = XMLUtils.getElements(doc.getDocumentElement(), "soap:Header/wsse:Security/ds:Signature");
		System.out.println("SignatureUtils.verifyHeaderSignature(), [length:" + elHeaderSignatures.getLength() + "]");
		for (int i = 0; i < elHeaderSignatures.getLength(); i++) {
			try {
				CertificateInfo certInfo = verifyXMLDSIGImpl(XMLDSIGScope.HEADER, fac, doc, elHeaderSignatures.item(i));
				certs.add(certInfo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return certs;
	}
	
	public static Set<CertificateInfo> verifyAppDataSignatures(Document doc) throws TransformerException {
		System.out.println("SignatureUtils.verifyAppDataSignatures()");
		NodeList nl = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
		if (nl.getLength() == 0) {
			throw new IllegalArgumentException("Не найден элемент Signature.");
		}

		NodeList elIdElems = XMLUtils.getElements(doc.getDocumentElement(), "//*[@Id or @wsu:Id]");
		for (int i = 0; i < elIdElems.getLength(); i++) {
			Node idElemCur = elIdElems.item(i);
			Element bodyElement = (Element) idElemCur;
			System.out.println("   " + idElemCur.getNodeName());
			Attr idAttr = bodyElement.getAttributeNodeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");
			if (idAttr != null) {
				bodyElement.setIdAttributeNode(idAttr, true);
			} else {
				idAttr = bodyElement.getAttributeNodeNS(null, "Id");
				if (idAttr != null) {
					bodyElement.setIdAttributeNode(idAttr, true);
				}
			}
		}

		XMLSignatureFactory fac = Infrastructure.getXMLSignatureFactory();

		Set<CertificateInfo> certs = new HashSet<CertificateInfo>();

		NodeList elAppDataSignatures = XMLUtils.getElements(doc.getDocumentElement(), "//smev:MessageData/smev:AppData/ds:Signature");
		System.out.println("SignatureUtils.verifyAppDataSignatures(), [length:" + elAppDataSignatures.getLength() + "]");
		for (int i = 0; i < elAppDataSignatures.getLength(); i++) {
			try {
				CertificateInfo certInfo = verifyXMLDSIGImpl(XMLDSIGScope.APP_DATA, fac, doc, elAppDataSignatures.item(i));
				certs.add(certInfo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return certs;
	}
	
	public static void verifyAppDataArchive(String filename) {
	}

	// ******************* utils *******************

	public static enum XMLDSIGScope {
		HEADER, APP_DATA;
	}

	private static class KeySignPair {
		public Element sigE;
		public Node keyE;

		public KeySignPair(Element sigE, Node keyE) {
			this.sigE = sigE;
			this.keyE = keyE;
		}
	}

	private static KeySignPair signXMLDSIGImpl(XMLDSIGScope scope, Document doc, Element signContextElement, String refId, PrivateKey privateKey, X509Certificate cert)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, TransformerException, MarshalException, XMLSignatureException, TransformationException {
		final Transforms transforms = new Transforms(doc);
		transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);

		XMLSignatureFactory fac = Infrastructure.getXMLSignatureFactory();

		// Преобразования над блоком SignedInfo
		List<Transform> transformList = getTransforms(scope, doc);

		// Ссылка на подписываемые данные.
		Reference ref = fac.newReference(refId, fac.newDigestMethod("http://www.w3.org/2001/04/xmldsig-more#gostr3411", null), transformList, null, null);

		// Блок SignedInfo.
		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null),
				fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411", null), Collections.singletonList(ref));

		// Блок KeyInfo.
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		X509Data x509d = kif.newX509Data(Collections.singletonList(cert));
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

		// Подпись данных.
		javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);
		DOMSignContext signContext = new DOMSignContext(privateKey, signContextElement);
		sig.sign(signContext);
//		System.out.println("ref: " + Base64.encode(ref.getDigestValue()));
//		System.out.println("sig: " + Base64.encode(sig.getSignatureValue().getValue()));

		// Блок подписи Signature.
		Element sigE = (Element) XPathAPI.selectSingleNode(signContext.getParent(), "//ds:Signature");
		// Блок данных KeyInfo.
		Node keyE = XPathAPI.selectSingleNode(sigE, "//ds:KeyInfo", sigE);
//		System.out.println("sigE: " + sigE.getTextContent());
//		System.out.println("keyE: " + keyE.getTextContent());

		return new KeySignPair(sigE, keyE);
	}

	private static CertificateInfo verifyXMLDSIGImpl(XMLDSIGScope scope, XMLSignatureFactory fac, Document doc, Node signatureNode)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, TransformerException, MarshalException, XMLSignatureException, TransformationException {
		try {
			X509Certificate cert;
			CertificateInfo certInfo;
			if (scope == XMLDSIGScope.HEADER) {
				String actor = signatureNode.getParentNode().getAttributes().getNamedItemNS("http://schemas.xmlsoap.org/soap/envelope/", "actor").getTextContent();
				System.out.println("SignatureUtils.verifyXMLDSIGImpl(HEADER), [signature.actor:" + actor + "]");
				
				Node certBase64Node = XMLUtils.getElement(signatureNode.getParentNode(), "wsse:BinarySecurityToken");
				X509Security x509 = new X509Security((Element) certBase64Node);
				cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(x509.getToken()));
				certInfo = new CertificateInfo(cert, certBase64Node.getTextContent());
			} else {
				Node certBase64Node = XMLUtils.getElement(signatureNode, "ds:KeyInfo/ds:X509Data/ds:X509Certificate");
				String certBase64 = certBase64Node.getTextContent().replaceAll("[\\r\\n]", "");
				cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(Base64.decode(certBase64)));
				certInfo = new CertificateInfo(cert, certBase64Node.getTextContent());
			}

			CertificateUtils.verifyCertificate(certInfo.getCertBase64());

			DOMValidateContext context = new DOMValidateContext(cert.getPublicKey(), signatureNode);
			XMLSignature signature = fac.unmarshalXMLSignature(context);
			System.out.println("In main: signature = [" + signature + "]");

			boolean coreValidity = signature.validate(context);
			System.out.println("SignatureUtils.verifyHeaderSignature(), [coreValidity:" + coreValidity + "]");

//			System.out.println("digest: " + Base64.encode(ref.getDigestValue()));
			return certInfo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static List<Transform> getTransforms(XMLDSIGScope scope, Document doc) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		List<Transform> transformList = new ArrayList<Transform>();
		switch (scope) {
		case HEADER:
			Transform transformC14N = Infrastructure.getXMLSignatureFactory().newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
			transformList.add(transformC14N);
			break;
		case APP_DATA:
			Transform transformEnvSignature2 = Infrastructure.getXMLSignatureFactory().newTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE, (XMLStructure) null);
			Transform transformC14N2 = Infrastructure.getXMLSignatureFactory().newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS, (XMLStructure) null);
			transformList.add(transformEnvSignature2);
			transformList.add(transformC14N2);
			break;
		}
		return transformList;
	}

}
