package com.agiliumlabs.smev.ws.ds.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.util.Base64;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.CryptoPro.JCP.JCP;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;
import sun.security.pkcs.ParsingException;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import com.agiliumlabs.smev.ws.ds.utils.CertificateUtils.CertificateInfo;

public class PKCS7Utils {

	public static byte[] createPKCS7(PrivateKey privateKey, X509Certificate cert, byte[] fileData) throws NoSuchAlgorithmException, IllegalArgumentException, IOException, SignatureException,
			InvalidKeyException {
		// calculate message digest
		MessageDigest md = MessageDigest.getInstance(JCP.GOST_DIGEST_NAME);
		md.update(fileData);
		byte[] fileDigest = md.digest();

		// construct authenticated attributes...
		PKCS9Attribute[] authenticatedAttributeList = { new PKCS9Attribute(PKCS9Attribute.CONTENT_TYPE_OID, ContentInfo.DATA_OID), new PKCS9Attribute(PKCS9Attribute.SIGNING_TIME_OID, new Date()),
				new PKCS9Attribute(PKCS9Attribute.MESSAGE_DIGEST_OID, fileDigest) };

		PKCS9Attributes authenticatedAttributes = new PKCS9Attributes(authenticatedAttributeList);

		Signature sign = Signature.getInstance(JCP.GOST_EL_SIGN_NAME/*, PROVIDER_NAME*/);
		sign.initSign(privateKey);
		sign.update(authenticatedAttributes.getDerEncoding());
		byte[] signedAttributes = sign.sign();

		ContentInfo contentInfo = null;
		// We can attach the data here, or not
//		if (attachFile) {
//			contentInfo = new ContentInfo(ContentInfo.DATA_OID, new DerValue(DerValue.tag_OctetString, fileData));
//		} else {
		contentInfo = new ContentInfo(ContentInfo.DATA_OID, null);
//		}

		X509Certificate[] certs = { cert };

		SignerInfo si = new SignerInfo(new X500Name(cert.getIssuerDN().getName()), cert.getSerialNumber(), AlgorithmId.get(JCP.GOST_DIGEST_NAME), authenticatedAttributes,
				AlgorithmId.get("GOST3410EL"), signedAttributes, null);
		SignerInfo[] signer = { si };

		AlgorithmId[] digestAlgorithmIds = { AlgorithmId.get(JCP.GOST_DIGEST_NAME) };
		PKCS7 p7 = new PKCS7(digestAlgorithmIds, contentInfo, certs, signer);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		p7.encodeSignedData(out);
		out.flush();
		out.close();
		return out.toByteArray();
	}

	public static Boolean verifyPKCS7(X509Certificate cert, byte[] p7sBytes, byte[] fileData) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		PKCS7 p7 = new PKCS7(p7sBytes);
		X509Certificate[] certs = p7.getCertificates();
		PublicKey pub = certs[0].getPublicKey();
		SignerInfo[] si = p7.getSignerInfos();
		byte[] contentData = null;
		if (null != fileData) {
			contentData = fileData;
		} else if (null != p7.getContentInfo().getData()) {
			contentData = p7.getContentInfo().getData();
		} else {
			System.out.println("p7s file not contains data");
			return false;
		}
//		if (null != fileToSaveData) {
//			FileUtils.saveFile(fileToSaveData, contentData);
//		}
		MessageDigest md = MessageDigest.getInstance(JCP.GOST_DIGEST_NAME);
		md.update(contentData);
		byte[] digestedContent = md.digest();
		byte[] digest = (byte[]) si[0].getAuthenticatedAttributes().getAttributeValue(PKCS9Attribute.MESSAGE_DIGEST_OID);
		if (!Arrays.equals(digestedContent, digest)) {
			System.out.println("File digest (hash) is not valid. Signature is false");
			return false;
		}

//		checkKeyUsage(certs[0].getKeyUsage());

		Signature sign = Signature.getInstance(JCP.GOST_EL_SIGN_NAME/*, PROVIDER_NAME*/);
		sign.initVerify(pub);
		sign.update(si[0].getAuthenticatedAttributes().getDerEncoding());
		Boolean isCorrected = sign.verify(si[0].getEncryptedDigest());
		System.out.println("DS PKCS#7 verification [isCorrected:" + isCorrected + "]");
		return isCorrected;
	}

	public static void verifyInsideArchives(Set<CertificateInfo> certs, Document doc) throws TransformerException, IOException, WSSecurityException {
		System.out.println("PKCS7Utils.verifyInsideArchives()");
		NodeList elMessageDataArchives = XMLUtils.getElements(doc.getDocumentElement(), "//smev:MessageData/smev:AppDocument/smev:BinaryData");
		if (elMessageDataArchives.getLength() == 0) {
			System.err.println("Archive verification failed. There is no smev:BinaryData node");
		}
		for (int i = 0; i < elMessageDataArchives.getLength(); i++) {
			Node elemCur = elMessageDataArchives.item(i);
			String archiveBase64Str = elemCur.getTextContent();
			Node requestCodeNode = XMLUtils.getElement(elemCur.getParentNode(), "smev:RequestCode");
			System.out.println("PKCS7Utils.verifyInsideArchives(), [requestCode:" + requestCodeNode.getTextContent() + "]");

			ZipInputStream stream = new ZipInputStream(new ByteArrayInputStream(Base64.decode(archiveBase64Str)));
			ZipEntry entry;
			Map<String, byte[]> zipFiles = new HashMap<String, byte[]>();
			while ((entry = stream.getNextEntry()) != null) {
				System.out.println("    entry [name:" + entry.getName() + "]");

				final int bufsize = 4096;
				byte[] data = new byte[bufsize];
				int used = 0;
				while (true) {
					if (data.length - used < bufsize) {
						byte[] newData = new byte[data.length << 1];
						System.arraycopy(data, 0, newData, 0, used);
						data = newData;
					}
					int got = stream.read(data, used, data.length - used);
					if (got <= 0)
						break;
					used += got;
				}
				byte[] finedData = new byte[used];
				System.arraycopy(data, 0, finedData, 0, used);
				zipFiles.put(entry.getName(), finedData);
			}

			// ********** verification ************
			Set<String> baseZipFiles = new HashSet<String>();
			for (String fileName : zipFiles.keySet()) {
				if (zipFiles.keySet().contains(fileName + ".sig")) {
					baseZipFiles.add(fileName);
					System.out.println("    base entry [name:" + fileName + "]");
				}
			}
			Boolean isVerified = false;
			for (CertificateInfo cert : certs) {
				try {
					for (String fileName : baseZipFiles) {
						verifyPKCS7(cert.getCert(), zipFiles.get(fileName + ".sig"), zipFiles.get(fileName));
					}
					isVerified = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (isVerified) {
				System.out.println("Archive verification [isCorrected:" + isVerified + "]");
			} else {
				System.err.println("Archive verification failed");
			}
		}
	}

}
