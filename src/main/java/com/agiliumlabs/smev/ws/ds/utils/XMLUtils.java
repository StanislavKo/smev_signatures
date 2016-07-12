package com.agiliumlabs.smev.ws.ds.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.agiliumlabs.smev.ws.ds.Infrastructure;

public class XMLUtils {

	public static Document readXMLFile(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(xmlFileName);
		Document doc = Infrastructure.getDocumentBuilder().parse(fXmlFile);
		return doc;
	}

	public static Document readXMLString(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		Document doc = Infrastructure.getDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
		return doc;
	}

	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			te.printStackTrace();
		}
		return sw.toString();
	}
	
	public static String getElementValue(Element element, String xpath) throws TransformerException {
		return ((Element) getElement(element, xpath)).getTextContent();
	}
	
	public static Node getElement(Node element, String xpath) throws TransformerException {
		Element nsResolverNode = element.getOwnerDocument().createElement("NamespaceResolver");
		nsResolverNode.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		nsResolverNode.setAttribute("xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		nsResolverNode.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
		nsResolverNode.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		nsResolverNode.setAttribute("xmlns:ws3318", "http://esv.server.rt.ru");
		nsResolverNode.setAttribute("xmlns:smev", "http://smev.gosuslugi.ru/rev120315");

		Node node = XPathAPI.selectSingleNode(element, xpath, nsResolverNode);
		return node;
	}
	
	public static NodeList getElements(Node element, String xpath) throws TransformerException {
		Element nsResolverNode = element.getOwnerDocument().createElement("NamespaceResolver");
		nsResolverNode.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		nsResolverNode.setAttribute("xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		nsResolverNode.setAttribute("xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
		nsResolverNode.setAttribute("xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		nsResolverNode.setAttribute("xmlns:ws3318", "http://esv.server.rt.ru");
		nsResolverNode.setAttribute("xmlns:smev", "http://smev.gosuslugi.ru/rev120315");

		NodeList nodes = XPathAPI.selectNodeList(element, xpath, nsResolverNode);
		return nodes;
	}
	
}
