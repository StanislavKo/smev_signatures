/**
 * 
 */
package com.agiliumlabs.smev.ws.impl.server;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.MTOM;

import com.agiliumlabs.smev.ws.Router;
import com.agiliumlabs.smev.ws.Server;
import com.agiliumlabs.smev.ws.converters.Rev120315Converter;
import com.agiliumlabs.smev.ws.model.Header;
import com.agiliumlabs.smev.ws.model.Message;
import com.agiliumlabs.smev.ws.model.MessageData;
import com.agiliumlabs.smev.ws.model.SmevData;

import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.MessageDataType;
import ru.gosuslugi.smev.rev120315.MessageType;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceMessageData;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceResponseMessageData;
import ru.gosuslugi.smev.rev120315.SyncRequestType;
import ru.gosuslugi.smev.rev120315.SyncResponseType;

/**
 * @author roman
 *
 */
//@javax.xml.ws.BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING)
@WebService (name = "SmevServer", targetNamespace="http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
//@XmlSeeAlso(value = {org.w3._2004._08.xop.include.ObjectFactory.class})
@MTOM(enabled=true, threshold=5*1024*1024)
@HandlerChain(file = "response-handler-chain.xml")
public class SmevServer {

	@Inject
	@Server
	private Router router;
	@Inject
	private Rev120315Converter converter;
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.impl.server.SmevServer#syncReq(ru.gosuslugi.smev.rev120315.HeaderType, ru.gosuslugi.smev.msgexample.xsd.types.SyncRequestType)
	 */
	@WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/syncReq")
    @WebResult(name = "SyncResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "syncReqResponseParameters")
    public SyncResponseType syncReq(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "SyncRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "syncReqParameters")
        SyncRequestType parameters) {
		SmevData req = convertFromSmev(smevHeader, parameters);
		SmevData res = router.syncRequest(new ArrayList<Handler>(), req);
		return convertToSmev(res); 
	}

	private SyncResponseType convertToSmev(SmevData res) {
		MessageType msg = converter.convertToSmev(res.getMessage());
		ProcessCanonicalServiceResponseMessageData data = converter.convertToSmevResponse(res.getData());
		SyncResponseType result = new SyncResponseType();
		result.setMessage(msg);
		result.setMessageData(data);
		return result;
	}

	private SmevData convertFromSmev(HeaderType smevHeader,
			SyncRequestType parameters) {
		Header hdr = converter.convertFromSmev(smevHeader);
		Message msg = converter.convertFromSmev(parameters.getMessage());
//		MessageData data = converter.convertFromSmev(parameters.getMessageData());
		SmevData result = new SmevData();
//		result.setData(data);
		result.setHeader(hdr);
		result.setMessage(msg);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.impl.server.SmevServer#aSyncReq(ru.gosuslugi.smev.rev120315.HeaderType, ru.gosuslugi.smev.msgexample.xsd.types.SyncRequestType)
	 */
	@WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/aSyncReq")
    @WebResult(name = "aSyncReqResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "asyncReqResponseParameters")
    public SyncResponseType aSyncReq(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "aSyncReqRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "asyncReqParameters")
        SyncRequestType parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.impl.server.SmevServer#aSyncResult(ru.gosuslugi.smev.rev120315.HeaderType, ru.gosuslugi.smev.msgexample.xsd.types.SyncRequestType)
	 */
	@WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/aSyncResult")
    @WebResult(name = "aSyncResultResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "aSyncResultResponseParameters")
    public SyncResponseType aSyncResult(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "aSyncResultRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "aSyncResultParameters")
        SyncRequestType parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
