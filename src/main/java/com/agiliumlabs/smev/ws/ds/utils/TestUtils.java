package com.agiliumlabs.smev.ws.ds.utils;

import java.awt.Toolkit;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ru.gosuslugi.smev.msgexample.xsd.types.ProcessCanonicalServiceMessageType;
import ru.gosuslugi.smev.rev120315.AppDocumentType;
import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.MessageClassType;
import ru.gosuslugi.smev.rev120315.MessageType;
import ru.gosuslugi.smev.rev120315.OrgExternalType;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceMessageData;
import ru.gosuslugi.smev.rev120315.StatusType;
import ru.gosuslugi.smev.rev120315.SubMessageType;
import ru.gosuslugi.smev.rev120315.SubMessagesType;
import ru.gosuslugi.smev.rev120315.SyncRequestType;
import ru.gosuslugi.smev.rev120315.TypeCodeType;

import com.agiliumlabs.smev.ws.ds.Consts;
import com.agiliumlabs.smev.ws.ds.Infrastructure;
import com.agiliumlabs.smev.ws.ds.utils.XMLUtils;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.StreamingDataHandler;

public class TestUtils {

	public static Header makeHeader(String nodeId, String messageId, String timeStamp, MessageClassType messageClass) throws JAXBException, DatatypeConfigurationException {
//		Element smevHeaderEl = Infrastructure.getDocumentBuilder().newDocument().createElementNS("http://smev.gosuslugi.ru/rev120315", "smev:Header");
		Element parentEl = Infrastructure.getDocumentBuilder().newDocument().createElement("parent");
		final Marshaller marshaller = JAXBContext.newInstance(HeaderType.class).createMarshaller();
		HeaderType headerType = createHeaderType(nodeId, messageId, timeStamp, messageClass);
		marshaller.marshal(new JAXBElement<HeaderType>(new QName("http://smev.gosuslugi.ru/rev120315","Header"), HeaderType.class, headerType), parentEl);
		
		Header header = Headers.create((Element) parentEl.getFirstChild());
		return header;
	}
	
	public static Element makeAppDocument() throws JAXBException, DatatypeConfigurationException, ParserConfigurationException {
//		Element smevHeaderEl = Infrastructure.getDocumentBuilder().newDocument().createElementNS("http://smev.gosuslugi.ru/rev120315", "smev:Header");
//		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//		documentBuilderFactory.setNamespaceAware(true);
//		documentBuilderFactory. setExpandEntityReferences(true);
//		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Element parentEl = Infrastructure.getDocumentBuilder().newDocument().createElement("parent");
		final Marshaller marshaller = JAXBContext.newInstance(AppDocumentType.class).createMarshaller();
		AppDocumentType appDocument = createAppDocumentInline();
		System.out.println(appDocument);
		marshaller.marshal(new JAXBElement<AppDocumentType>(new QName("http://smev.gosuslugi.ru/rev120315","AppDocument"), AppDocumentType.class, appDocument), parentEl);
		
		return (Element) parentEl.getFirstChild();
	}
	
	public static HeaderType createHeaderType(String nodeId, String messageId, String timeStamp, MessageClassType messageClass) throws DatatypeConfigurationException {
		HeaderType headerType = new ru.gosuslugi.smev.rev120315.ObjectFactory().createHeaderType();
		headerType.setNodeId(nodeId); 
		headerType.setMessageId(messageId);
		GregorianCalendar gregCal = (GregorianCalendar) GregorianCalendar.getInstance();
		gregCal.setTime(new Date());
		headerType.setTimeStamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		headerType.setMessageClass(messageClass);
		return headerType;
	}
	
	public static AppDocumentType createAppDocumentInline() throws DatatypeConfigurationException {
		AppDocumentType appDocument = new ru.gosuslugi.smev.rev120315.ObjectFactory().createAppDocumentType();
		appDocument.setRequestCode("");
		appDocument.setBinaryData(new byte[] {});
		return appDocument;
	}
	
	public static AppDocumentType createAppDocumentWithAttachment() throws DatatypeConfigurationException {
		AppDocumentType appDocument = new ru.gosuslugi.smev.rev120315.ObjectFactory().createAppDocumentType();
		appDocument.setRequestCode("");
		appDocument.setBinaryData(new byte[] {});
		appDocument.setDigestValue(new byte[] {});
		appDocument.setReference(new DataHandler(new FileDataSource(new File(Consts.FILE_ATTACHMENT_ZIP))));
		return appDocument;
	}
	
//	public static SendRegHydroBuildQueryRequest createAppDataRequest() throws DatatypeConfigurationException {
//		SendRegHydroBuildQueryRequest sendRegHydroBuildQueryRequest = createSimpleRequest();
//		
//		SendRegHydroBuildQueryType.AppData appData = new ru.favr.smev.spurrgts.ObjectFactory().createSendRegHydroBuildQueryTypeAppData();
//		appData.setFullName("Full Name");
//		appData.setHTBArea("HTBArea");
//		appData.setHTBCity("HTBCity");
//		appData.setHTBCode("HTBCode");
//		appData.setHTBName("HTBName");
//		appData.setHTBOwner("HTBOwner");
//		appData.setHTBRegionName("HTBRegionName");
//		appData.setInn("Inn");
//		appData.setOgrn("Ogrn");
//		appData.setWoName("WoName");
//		
//		sendRegHydroBuildQueryRequest.getMessageData().setAppData(appData);
//		
//		return sendRegHydroBuildQueryRequest;
//	}
//	
	public static SyncRequestType createSimpleRequest() throws DatatypeConfigurationException {
		SyncRequestType processCanonicalServiceMessage = new ru.gosuslugi.smev.rev120315.ObjectFactory().createSyncRequestType();
		ProcessCanonicalServiceMessageData processCanonicalServiceMessageData = new ru.gosuslugi.smev.rev120315.ObjectFactory().createProcessCanonicalServiceMessageData();
		MessageType messageType = new ru.gosuslugi.smev.rev120315.ObjectFactory().createMessageType();
		OrgExternalType orgExternalType = new ru.gosuslugi.smev.rev120315.ObjectFactory().createOrgExternalType();
		SubMessagesType subMessagesType = new ru.gosuslugi.smev.rev120315.ObjectFactory().createSubMessagesType();
		SubMessageType subMessageType = new ru.gosuslugi.smev.rev120315.ObjectFactory().createSubMessageType();
		
		subMessageType.setCaseNumber("CaseNumber_02");
		GregorianCalendar gregCal_02 = (GregorianCalendar) GregorianCalendar.getInstance();
		gregCal_02.setTime(new Date());
		subMessageType.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal_02));
		subMessageType.setOriginRequestIdRef("OriginRequestIdRef_02");
		subMessageType.setRequestIdRef("RequestIdRef_02");
		subMessageType.setServiceCode("ServiceCode_02");
		subMessageType.setOriginator(orgExternalType);
		subMessageType.setStatus(StatusType.REQUEST);
		subMessageType.setSubRequestNumber("SubRequestNumber_02");
		
		subMessagesType.getSubMessage().add(subMessageType);
		
		orgExternalType.setCode("CODE12345");
		orgExternalType.setName("name");
		
		messageType.setCaseNumber("CaseNumber");
		GregorianCalendar gregCal = (GregorianCalendar) GregorianCalendar.getInstance();
		gregCal.setTime(new Date());
		messageType.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		messageType.setExchangeType("ExchangeType");
		messageType.setOriginRequestIdRef("OriginRequestIdRef");
		messageType.setRequestIdRef("RequestIdRef");
		messageType.setServiceCode("ServiceCode");
		messageType.setServiceName("ServiceName");
		messageType.setTestMsg("TestMsg");
		messageType.setOriginator(orgExternalType);
		messageType.setRecipient(orgExternalType);
		messageType.setSender(orgExternalType);
		messageType.setStatus(StatusType.REQUEST);
		messageType.setSubMessages(subMessagesType);
		messageType.setTypeCode(TypeCodeType.GSRV);

		ProcessCanonicalServiceMessageType processCanonicalServiceMessageType = new ru.gosuslugi.smev.msgexample.xsd.types.ObjectFactory().createProcessCanonicalServiceMessageType();
		processCanonicalServiceMessageType.setId("Id");
		processCanonicalServiceMessageType.setTaskId("TaskId");
		processCanonicalServiceMessageData.setAppData(processCanonicalServiceMessageType);
		
		processCanonicalServiceMessage.setMessage(messageType);
		processCanonicalServiceMessage.setMessageData(processCanonicalServiceMessageData);
		
		return processCanonicalServiceMessage;
	}
	
}
