package com.agiliumlabs.smev.ws;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.w3c.dom.Element;

import ru.CryptoPro.JCP.JCP;
import ru.gosuslugi.smev.msgexample.MsgExampleService;
import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.MessageClassType;
import ru.gosuslugi.smev.rev120315.SyncRequestType;
import ru.gosuslugi.smev.rev120315.SyncResponseType;
import wss4j.utility.SpecUtility;

import com.agiliumlabs.smev.ws.ds.Consts;
import com.agiliumlabs.smev.ws.ds.Infrastructure;
import com.agiliumlabs.smev.ws.ds.handlers.FixNSHandler;
import com.agiliumlabs.smev.ws.ds.handlers.GenerateInnerArchiveHandler;
import com.agiliumlabs.smev.ws.ds.handlers.LogHandler;
import com.agiliumlabs.smev.ws.ds.handlers.SignAppDataHandler;
import com.agiliumlabs.smev.ws.ds.handlers.SignHeaderHandler;
import com.agiliumlabs.smev.ws.ds.utils.TestUtils;
import com.agiliumlabs.smev.ws.impl.server.SOAPLoggingHandler;
import com.agiliumlabs.smev.ws.impl.server.SmevServer;
import com.agiliumlabs.smev.ws.model.AppData;
import com.agiliumlabs.smev.ws.model.AppDocument;
import com.agiliumlabs.smev.ws.model.Header;
import com.agiliumlabs.smev.ws.model.Message;
import com.agiliumlabs.smev.ws.model.MessageClass;
import com.agiliumlabs.smev.ws.model.MessageData;
import com.agiliumlabs.smev.ws.model.Organization;
import com.agiliumlabs.smev.ws.model.PacketId;
import com.agiliumlabs.smev.ws.model.Service;
import com.agiliumlabs.smev.ws.model.SmevData;
import com.agiliumlabs.smev.ws.model.Status;
import com.agiliumlabs.smev.ws.model.TypeCode;
import com.agiliumlabs.smev.ws.sample.SmevTestServerReceiver;
import com.agiliumlabs.smev.ws.sample.TestData;
import com.sun.xml.ws.developer.WSBindingProvider;

@RunWith(LocalWeldRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestSmev {

	private static PrivateKey privateKey;
	private static X509Certificate cert;

	@Inject
	private SmevServer service;
	private Endpoint endpoint;
	@Inject @Client @Facade
	private SmevAgent clientRouter; 
	private Fixture data;
	@Inject @Server
	private SmevTestServerReceiver receiver;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			System.out.println("home:" + System.getProperty("java.home"));
			System.out.println("version:" + System.getProperty("java.version"));
			System.out.println("vm.name:" + System.getProperty("java.vm.name"));
			
			KeyStore keyStore = KeyStore.getInstance(JCP.HD_STORE_NAME);
			keyStore.load(null, null);  

			// Получение ключа и сертификата.
			privateKey = (PrivateKey) keyStore.getKey(SpecUtility.DEFAULT_ALIAS, SpecUtility.DEFAULT_PASSWORD);
			cert = (X509Certificate) keyStore.getCertificate(SpecUtility.DEFAULT_ALIAS);

			Infrastructure.init();
		} catch (Exception ex) {
			ex.printStackTrace(); 
		} 
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() {
		System.out.println("@BeforeClass setUp()");
		endpoint = Endpoint.publish("http://localhost:18080/smev", service);
//		try {
//			System.out.println(new java.util.Scanner(new URL("http://localhost:18080/smev?wsdl").openStream(), "UTF-8").useDelimiter("\\A").next());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		endpoint.getBinding().setHandlerChain(Arrays.asList((Handler)new SOAPLoggingHandler()));
	}
	
	@Before
	public void setUpFixture() {
		data = new Fixture();
	}
	
	@After
	public void tearDown() {
		endpoint.stop();
	}
	
//	@Test
//	public void testSyncRequest() {
//		SmevData msg = data.generateSmevData(false);
//		executeTest(msg);
//	}
//	
//	@Test
//	public void testSyncRequestMtom() {
//		SmevData msg = data.generateSmevData(true);
//		executeTest(msg);
//	}
		
	@Test
	public void test01SignSimpleDoc() throws Exception {
		System.out.println("\r\n\r\n\r\n\r\ntest01SignSimpleDoc &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding(); 
		binding.setMTOMEnabled(true);
		List<Handler> defaultHandlerList = null;
		try { 
			defaultHandlerList = binding.getHandlerChain(); 
			List<Handler> handlerList = new ArrayList<Handler>();
			handlerList.add(0, new FixNSHandler());
			handlerList.add(1, new SignHeaderHandler(privateKey, cert));
			handlerList.add(2, new LogHandler());
			
			SmevData msg = data.generateSmevData(SMEVAppDocument.NONE);
			executeTest(handlerList, msg);
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally { 
			if (defaultHandlerList != null) {
				binding.setHandlerChain(defaultHandlerList);
			}
		}
	}

	@Test
	public void test02SignAppDataElement01() throws Exception {
		System.out.println("\r\n\r\n\r\n\r\ntest02SignAppDataElement01 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding(); 
		binding.setMTOMEnabled(true);
		List<Handler> defaultHandlerList = null;
		try {
			defaultHandlerList = binding.getHandlerChain(); 
			List<Handler> handlerList = new ArrayList<Handler>();
			handlerList.add(0, new FixNSHandler());
			handlerList.add(1, new SignAppDataHandler(privateKey, cert));
			handlerList.add(2, new SignHeaderHandler(privateKey, cert));
			handlerList.add(3, new LogHandler());
			
			SmevData msg = data.generateSmevData(SMEVAppDocument.NONE);
			executeTest(handlerList, msg);
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally { 
			if (defaultHandlerList != null) {
				binding.setHandlerChain(defaultHandlerList);
			}
		}
	}

	@Test
	public void test04SignInnerAttachmentDoc01() throws Exception {
		System.out.println("\r\n\r\n\r\n\r\ntest04SignInnerAttachmentDoc01 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding(); 
		binding.setMTOMEnabled(true);
		List<Handler> defaultHandlerList = null;
		try {
			defaultHandlerList = binding.getHandlerChain(); 
			List<Handler> handlerList = new ArrayList<Handler>();
			handlerList.add(0, new FixNSHandler());
			handlerList.add(1, new GenerateInnerArchiveHandler(privateKey, cert, Consts.FILE_ARCHIVE_FOLDER, Consts.FILE_ARCHIVE_REQUEST_CODE));
			handlerList.add(2, new SignHeaderHandler(privateKey, cert));
			handlerList.add(3, new LogHandler());
			
			SmevData msg = data.generateSmevData(SMEVAppDocument.NONE);
			executeTest(handlerList, msg);
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally { 
			if (defaultHandlerList != null) {
				binding.setHandlerChain(defaultHandlerList);
			}
		}
	}

	@Test
	public void test05SignMTOMAttachmentDoc01() throws Exception {
		System.out.println("\r\n\r\n\r\n\r\ntest05SignMTOMAttachmentDoc01 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding(); 
		binding.setMTOMEnabled(true);
		List<Handler> defaultHandlerList = null;
		try {
			defaultHandlerList = binding.getHandlerChain(); 
			List<Handler> handlerList = new ArrayList<Handler>();
			
			SmevData msg = data.generateSmevData(SMEVAppDocument.MTOM);
			executeTest(handlerList, msg);
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally { 
			if (defaultHandlerList != null) {
				binding.setHandlerChain(defaultHandlerList);
			}
		}
	}

	private void executeTest(List<Handler> handlerList, SmevData msg) {
		clientRouter.syncRequest(handlerList, msg);
		SmevData received = receiver.getMessage();
//		assertEquals(1, received.getData().getData().getData().size());
//		Element data = (Element) received.getData().getData().getData().get(0);
//		assertEquals("http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", data.getNamespaceURI());
//		assertEquals("Data", data.getFirstChild().getTextContent());
//		received.getData().getData().getData().set(0, new TestData("Data"));
//		assertEquals(msg, received);
	}
	
	private static enum SMEVAppDocument {
		NONE, INLINE, MTOM;
	}
	
	private static class Fixture {

		public SmevData generateSmevData(SMEVAppDocument attachment) {
			SmevData result = new SmevData();
			
			MessageData msgData = new MessageData();
			AppData appData = new AppData();
			Map<QName, String> appDataAttributes = new HashMap<QName, String>();
			appDataAttributes.put(new QName("test"), "testValue");
			appData.setAttributes(appDataAttributes);
			appData.setData(Arrays.asList(new Object[] {new TestData("Data")}));
			msgData.setData(appData);
			switch (attachment) {
			case NONE:
				break;
			case INLINE:
				break;
			case MTOM:
				AppDocument appDoc = new AppDocument();
				appDoc.setRequestCode("asdf");
				appDoc.setDigest(new byte[] {});
				appDoc.setReference(new DataHandler(new FileDataSource(new File(Consts.FILE_ATTACHMENT_ZIP))));
				msgData.setDocument(appDoc);
				break;
			}
			result.setData(msgData);
			
			Header hdr = new Header();
			hdr.setActor("http://smev.gosuslugi.ru/actors/smev");
			hdr.setMessageClass(MessageClass.REQUEST);
			hdr.setMessageId("messageId");
			hdr.setNodeId("nodeId");
			hdr.setTimestamp(new Date());
			List<PacketId> packetIds = new ArrayList<PacketId>();
			PacketId packetId = new PacketId();
			packetId.setMessageId("messageId");
			packetId.setSubRequestNumber("subRequestNumber");
			packetIds.add(packetId);
			hdr.setPacketIds(packetIds);
			Map<QName, String> attributes = new HashMap<QName, String>();
			attributes.put(new QName("testHdr"), "testValueHdr");
			hdr.setAttributes(attributes);
			result.setHeader(hdr);
			
			Message msg = new Message();
			msg.setCaseNumber("caseNumber");
			msg.setDate(new Date());
			msg.setExchangeType("exchangeType");
			msg.setOktmo("oktmo");
			Organization originator = new Organization("CODE12345", "originator");
			msg.setOriginator(originator);
			msg.setOriginRequestIdRef("originRequestIdRef");
			Organization recepient = new Organization("CODE12345", "recepient");
			msg.setRecepient(recepient);
			msg.setRequestIdRef("requestIdRef");
			Organization sender = new Organization("CODE12345", "sender");
			msg.setSender(sender);
			Service service = new Service();
			service.setMnemonic("mnemonic");
			service.setVersion("version");
//			msg.setService(service);
			msg.setServiceCode("serviceCode");
			msg.setServiceName("serviceName");
			msg.setStatus(Status.PING);
			msg.setSubMessages(null);
			msg.setTest("test");
			msg.setType(TypeCode.OTHR);
			result.setMessage(msg);
			return result;
		}

		private String repeat(String string, int iterations) {
			StringBuffer data = new StringBuffer();
			for (int i = 0; i < iterations; i++)
				data.append(string);
			return data.toString();
		}
		
	}
	
}
