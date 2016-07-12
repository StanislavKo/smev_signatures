/**
 * 
 */
package com.agiliumlabs.smev.ws.converters;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.agiliumlabs.smev.ws.model.AppData;
import com.agiliumlabs.smev.ws.model.AppDocument;
import com.agiliumlabs.smev.ws.model.Header;
import com.agiliumlabs.smev.ws.model.Message;
import com.agiliumlabs.smev.ws.model.MessageClass;
import com.agiliumlabs.smev.ws.model.MessageData;
import com.agiliumlabs.smev.ws.model.Organization;
import com.agiliumlabs.smev.ws.model.PacketId;
import com.agiliumlabs.smev.ws.model.Service;
import com.agiliumlabs.smev.ws.model.Status;
import com.agiliumlabs.smev.ws.model.SubMessage;
import com.agiliumlabs.smev.ws.model.TypeCode;

import ru.gosuslugi.smev.msgexample.xsd.types.ProcessCanonicalServiceMessageType;
import ru.gosuslugi.smev.msgexample.xsd.types.ProcessCanonicalServiceResponseMessageType;
import ru.gosuslugi.smev.rev120315.AppDataType;
import ru.gosuslugi.smev.rev120315.AppDocumentType;
import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.MessageClassType;
import ru.gosuslugi.smev.rev120315.MessageDataType;
import ru.gosuslugi.smev.rev120315.MessageType;
import ru.gosuslugi.smev.rev120315.OrgExternalType;
import ru.gosuslugi.smev.rev120315.PacketIdType;
import ru.gosuslugi.smev.rev120315.PacketIdsType;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceMessageData;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceResponseMessageData;
import ru.gosuslugi.smev.rev120315.ServiceType;
import ru.gosuslugi.smev.rev120315.StatusType;
import ru.gosuslugi.smev.rev120315.SubMessageType;
import ru.gosuslugi.smev.rev120315.SubMessagesType;
import ru.gosuslugi.smev.rev120315.TypeCodeType;
import javax.xml.namespace.QName;

/**
 * @author roman
 *
 */
public class Rev120315Converter {

	private DatatypeFactory df;
	
	public Rev120315Converter() {
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ProcessCanonicalServiceMessageData convertToSmev(MessageData data) {
		if (data != null) {
			ProcessCanonicalServiceMessageData result = new ProcessCanonicalServiceMessageData();
			System.out.println("    convertToSmev 1:" + data.getData());
			System.out.println("    convertToSmev 2:" + convertToSmev(data.getData()));
			if (data.getData() != null && data.getData().getAttributes() != null) {
				for (QName name : data.getData().getAttributes().keySet()) {
					System.out.println("        convertToSmev 3, [" + name + ":" + data.getData().getAttributes().get(name) + "]");
				}
			}
			result.setAppData(convertToSmev(data.getData()));
			result.setAppDocument(convertToSmev(data.getDocument()));
			if (result.getAppData() != null) {
				System.out.println("        convertToSmev 4:" + result.getAppData().getId());
//				for (QName name : result.getAppData().getOtherAttributes().keySet()) {
//					System.out.println("        convertToSmev 4, [" + name + ":" + result.getAppData().getOtherAttributes().get(name) + "]");
//				}
			}
			return result;
		}
		return null;
	}
	
	public ProcessCanonicalServiceResponseMessageData convertToSmevResponse(MessageData data) {
		if (data != null) {
			ProcessCanonicalServiceResponseMessageData result = new ProcessCanonicalServiceResponseMessageData();
			System.out.println("    convertToSmev 1:" + data.getData());
			System.out.println("    convertToSmev 2:" + convertToSmev(data.getData()));
			if (data.getData() != null && data.getData().getAttributes() != null) {
				for (QName name : data.getData().getAttributes().keySet()) {
					System.out.println("        convertToSmev 3, [" + name + ":" + data.getData().getAttributes().get(name) + "]");
				}
			}
			result.setAppData(convertToSmevResponse(data.getData()));
			result.setAppDocument(convertToSmev(data.getDocument()));
			if (result.getAppData() != null) {
				System.out.println("        convertToSmev 4:" + result.getAppData().getId());
//				for (QName name : result.getAppData().getOtherAttributes().keySet()) {
//					System.out.println("        convertToSmev 4, [" + name + ":" + result.getAppData().getOtherAttributes().get(name) + "]");
//				}
			}
			return result;
		}
		return null;
	}
	
	private AppDocumentType convertToSmev(AppDocument document) {
		if (document != null) {
			AppDocumentType result = new AppDocumentType();
			result.setBinaryData(document.getBinaryData());
			result.setDigestValue(document.getDigest());
			result.setRequestCode(document.getRequestCode());
			result.setReference(document.getReference());
			return result;
		}
		return null;
	}

	private ProcessCanonicalServiceMessageType convertToSmev(AppData data) {
		if (data != null) {
			ProcessCanonicalServiceMessageType result = new ProcessCanonicalServiceMessageType();
			result.setId("123");//getOtherAttributes().clear();
//			result.getOtherAttributes().putAll(data.getAttributes());
//			result.getAny().clear();
//			result.getAny().addAll(data.getData());
			return result;
		}
		return null;
	}

	private ProcessCanonicalServiceResponseMessageType convertToSmevResponse(AppData data) {
		if (data != null) {
			ProcessCanonicalServiceResponseMessageType result = new ProcessCanonicalServiceResponseMessageType();
			result.setId("123");//getOtherAttributes().clear();
//			result.getOtherAttributes().putAll(data.getAttributes());
//			result.getAny().clear();
//			result.getAny().addAll(data.getData());
			return result;
		}
		return null;
	}

	public MessageData convertFromSmev(ProcessCanonicalServiceResponseMessageData data) {
		if (data != null) {
			MessageData result = new MessageData();
			result.setData(convertFromSmev(data.getAppData()));
			result.setDocument(convertFromSmev(data.getAppDocument()));
			return result;
		}
		return null;
	}
	
	public AppData convertFromSmev(ProcessCanonicalServiceResponseMessageType data) {
		if (data != null) {
			AppData result = new AppData();
//			result.setAttributes(data.getOtherAttributes());
//			result.setData(data.getAny());
			return result;
		}
		return null;
	}
	
	public MessageData convertFromSmev(MessageDataType data) {
		if (data != null) {
			MessageData result = new MessageData();
			result.setData(convertFromSmev(data.getAppData()));
			result.setDocument(convertFromSmev(data.getAppDocument()));
			return result;
		}
		return null;
	}
	
	public AppDocument convertFromSmev(AppDocumentType document) {
		if (document != null) {
			AppDocument result = new AppDocument();
			result.setBinaryData(document.getBinaryData());
			result.setDigest(document.getDigestValue());
			result.setRequestCode(document.getRequestCode());
			return result;
		}
		return null;
	}
	
	public AppData convertFromSmev(AppDataType data) {
		if (data != null) {
			AppData result = new AppData();
			result.setAttributes(data.getOtherAttributes());
			result.setData(data.getAny());
			return result;
		}
		return null;
	}
	
	public MessageType convertToSmev(Message message) {
		if (message != null) {
			MessageType result = new MessageType();
			result.setCaseNumber(message.getCaseNumber());
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(message.getDate().getTime());
			result.setDate(df.newXMLGregorianCalendar(gc));
			result.setExchangeType(message.getExchangeType());
			result.setOKTMO(message.getOktmo());
			result.setOriginator(convertToSmev(message.getOriginator()));
			result.setOriginRequestIdRef(message.getOriginRequestIdRef());
			result.setRecipient(convertToSmev(message.getRecepient()));
			result.setRequestIdRef(message.getRequestIdRef());
			result.setSender(convertToSmev(message.getSender()));
			result.setService(convertToSmev(message.getService()));
			result.setStatus(convertToSmev(message.getStatus()));
			result.setSubMessages(convertSubMessagesToSmev(message.getSubMessages()));
			result.setTestMsg(message.getTest());
			result.setTypeCode(convertToSmev(message.getType()));
			result.setServiceCode(message.getServiceCode());
			result.setServiceName(message.getServiceName());
			return result;
		}
		return null;
	}
	
	private SubMessagesType convertSubMessagesToSmev(
			List<SubMessage> subMessages) {
		if (subMessages != null) {
			SubMessagesType result = new SubMessagesType();
			for (SubMessage subMessage : subMessages) {
				SubMessageType sm = new SubMessageType();
				sm.setCaseNumber(subMessage.getCaseNumber());
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(subMessage.getDate().getTime());
				sm.setDate(df.newXMLGregorianCalendar(gc));
				sm.setOriginator(convertToSmev(subMessage.getOriginator()));
				sm.setOriginRequestIdRef(subMessage.getOriginRequestIdRef());
				sm.setRequestIdRef(subMessage.getRequestIdRef());
				sm.setServiceCode(subMessage.getServiceCode());
				sm.setStatus(convertToSmev(subMessage.getStatus()));
				sm.setSubRequestNumber(subMessage.getSubRequestNumber());
				result.getSubMessage().add(sm);
			}
			return result;
		}
		return null;
	}

	private TypeCodeType convertToSmev(TypeCode type) {
		return type != null ? TypeCodeType.valueOf(type.name()) : null;
	}

	private StatusType convertToSmev(Status status) {
		return status != null ? StatusType.valueOf(status.name()) : null;
	}

	private ServiceType convertToSmev(Service service) {
		if (service != null) {
			ServiceType result = new ServiceType();
			result.setMnemonic(service.getMnemonic());
			result.setVersion(service.getVersion());
			return result;
		} 
		return null;
	}

	private OrgExternalType convertToSmev(Organization organization) {
		if (organization != null) {
			OrgExternalType result = new OrgExternalType();
			result.setCode(organization.getCode());
			result.setName(organization.getName());
			return result;
		}
		return null;
	}

	public Message convertFromSmev(MessageType message) {
		if (message != null) {
			Message result = new Message();
			result.setCaseNumber(message.getCaseNumber());
			result.setDate(message.getDate().toGregorianCalendar().getTime());
			result.setExchangeType(message.getExchangeType());
			result.setOktmo(message.getOKTMO());
			result.setOriginator(convertFromSmev(message.getOriginator()));
			result.setOriginRequestIdRef(message.getOriginRequestIdRef());
			result.setRecepient(convertFromSmev(message.getRecipient()));
			result.setRequestIdRef(message.getRequestIdRef());
			result.setSender(convertFromSmev(message.getSender()));
			result.setService(convertFromSmev(message.getService()));
			result.setServiceCode(message.getServiceCode());
			result.setServiceName(message.getServiceName());
			result.setStatus(convertFromSmev(message.getStatus()));
			result.setSubMessages(convertFromSmev(message.getSubMessages()));
			result.setTest(message.getTestMsg());
			result.setType(convertFromSmev(message.getTypeCode()));
			return result;
		} 
		return null;
	}
	
	private List<SubMessage> convertFromSmev(SubMessagesType subMessages) {
		if (subMessages != null) {
			List<SubMessage> result = new ArrayList<SubMessage>();
			for (SubMessageType subMessage : subMessages.getSubMessage()) {
				SubMessage sm = new SubMessage();
				sm.setCaseNumber(subMessage.getCaseNumber());
				sm.setDate(subMessage.getDate().toGregorianCalendar().getTime());
				sm.setOriginator(convertFromSmev(subMessage.getOriginator()));
				sm.setOriginRequestIdRef(subMessage.getOriginRequestIdRef());
				sm.setRequestIdRef(subMessage.getRequestIdRef());
				sm.setServiceCode(subMessage.getServiceCode());
				sm.setStatus(convertFromSmev(subMessage.getStatus()));
				sm.setSubRequestNumber(subMessage.getSubRequestNumber());
				result.add(sm);
			}
			return result;
		}
		return null;
	}

	private Service convertFromSmev(ServiceType service) {
		if (service != null) {
			Service result = new Service();
			result.setMnemonic(service.getMnemonic());
			result.setVersion(service.getVersion());
			return result;
		}
		return null;
	}

	private Status convertFromSmev(StatusType status) {
		return status != null ? Status.valueOf(status.name()) : null;
	}

	private TypeCode convertFromSmev(TypeCodeType typeCode) {
		return typeCode != null ? TypeCode.valueOf(typeCode.name()) : null;
	}

	private Organization convertFromSmev(OrgExternalType organization) {
		if (organization != null) {
			Organization result = new Organization();
			result.setCode(organization.getCode());
			result.setName(organization.getName());
			return result;
		}
		return null;
	}

	public HeaderType convertToSmev(Header header) {
		if (header != null) {
			HeaderType result = new HeaderType();
			result.setActor(header.getActor());
			result.getOtherAttributes().putAll(header.getAttributes());
			result.setMessageClass(convertToSmev(header.getMessageClass()));
			result.setMessageId(header.getMessageId());
			result.setNodeId(header.getNodeId());
			result.setPacketIds(convertToSmev(header.getPacketIds()));
			if (header.getTimestamp() != null) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(header.getTimestamp().getTime());
				result.setTimeStamp(df.newXMLGregorianCalendar(gc));
			}
			return result;
		}
		return null;
	}
	
	private PacketIdsType convertToSmev(List<PacketId> packetIds) {
		if (packetIds != null) {
			PacketIdsType result = new PacketIdsType();
			for (PacketId packetId : packetIds) {
				PacketIdType id = new PacketIdType();
				id.setMessageId(packetId.getMessageId());
				id.setSubRequestNumber(packetId.getSubRequestNumber());
				result.getId().add(id);
			}
			return result;
		}
		return null;
	}

	private MessageClassType convertToSmev(MessageClass messageClass) {
		if (messageClass != null) {
			if (messageClass == MessageClass.REQUEST)
				return MessageClassType.REQUEST;
			else 
				return MessageClassType.RESPONSE;
		} 
		return null;
	}

	public Header convertFromSmev(HeaderType header) {
		if (header != null) {
			Header result = new Header();
			result.setActor(header.getActor());
			result.setAttributes(header.getOtherAttributes());
			result.setMessageId(header.getMessageId());
			result.setNodeId(header.getNodeId());
			result.setTimestamp(header.getTimeStamp().toGregorianCalendar().getTime());
			result.setMessageClass(convertFromSmev(header.getMessageClass()));
			result.setPacketIds(convertFromSmev(header.getPacketIds()));
			return result;
		}
		return null;
	}

	private List<PacketId> convertFromSmev(PacketIdsType packetIds) {
		if (packetIds != null) {
			List<PacketId> result = new ArrayList<PacketId>();
			for (PacketIdType packetId : packetIds.getId()) {
				PacketId id = new PacketId();
				id.setMessageId(packetId.getMessageId());
				id.setSubRequestNumber(packetId.getSubRequestNumber());
				result.add(id);
			}
			return result;
		} 
		return null;
	}

	private MessageClass convertFromSmev(MessageClassType messageClass) {
		if (messageClass != null) {
			if (messageClass == MessageClassType.REQUEST)
				return MessageClass.REQUEST;
			else 
				return MessageClass.RESPONSE;
		}
		return null;
	}
	
}
