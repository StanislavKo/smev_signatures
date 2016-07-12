/**
 * 
 */
package com.agiliumlabs.smev.ws.sample;

import java.util.List;

import javax.inject.Inject;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;

import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.MessageDataType;
import ru.gosuslugi.smev.rev120315.MessageType;
import ru.gosuslugi.smev.rev120315.ProcessCanonicalServiceMessageData;
import ru.gosuslugi.smev.rev120315.SyncRequestType;
import ru.gosuslugi.smev.rev120315.SyncResponseType;

import com.agiliumlabs.smev.ws.Client;
import com.agiliumlabs.smev.ws.Endpoint;
import com.agiliumlabs.smev.ws.SmevAgent;
import com.agiliumlabs.smev.ws.converters.Rev120315Converter;
import com.agiliumlabs.smev.ws.model.Header;
import com.agiliumlabs.smev.ws.model.Message;
import com.agiliumlabs.smev.ws.model.MessageData;
import com.agiliumlabs.smev.ws.model.SmevData;
import com.sun.xml.ws.developer.WSBindingProvider;

/**
 * @author roman
 *
 */
@Client
@Endpoint
public class SmevTestClient implements SmevAgent {

	@Inject
	private Rev120315Converter converter;
	private SmevServerService service;
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.SmevClient#syncRequest(com.agiliumlabs.smev.ws.model.SmevData)
	 */
	@Override
	public SmevData syncRequest(List<Handler> handlerList, SmevData message) {
		HeaderType header = converter.convertToSmev(message.getHeader());
		SyncRequestType params = convertRequest(message);
		SmevServer smevServer = getService().getSmevServerPort();
		WSBindingProvider bindingProvider = (WSBindingProvider) smevServer;
		SOAPBinding binding = (SOAPBinding) bindingProvider.getBinding(); 
		binding.setMTOMEnabled(true);
		List<Handler> defaultHandlerList = null;
		try {
			defaultHandlerList = binding.getHandlerChain();
			binding.setHandlerChain(handlerList);
			
			SyncResponseType response = smevServer.syncReq(header, params);
			return convertResponse(header, response);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (defaultHandlerList != null) {
				binding.setHandlerChain(defaultHandlerList);
			}
		}
	}

	private SmevData convertResponse(HeaderType header,
			SyncResponseType response) {
		Header hdr = converter.convertFromSmev(header);
		Message msg = converter.convertFromSmev(response.getMessage());
		MessageData data = converter.convertFromSmev(response.getMessageData());
		SmevData result = new SmevData();
		result.setData(data);
		result.setHeader(hdr);
		result.setMessage(msg);
		return result;
	}

	private SyncRequestType convertRequest(SmevData message) {
		SyncRequestType params = new SyncRequestType();
		MessageType msg = converter.convertToSmev(message.getMessage());
		params.setMessage(msg);
//		MessageDataType data = converter.convertToSmev(message.getData());
		ProcessCanonicalServiceMessageData data = converter.convertToSmev(message.getData());
		params.setMessageData(data);
		return params;
	}

	private SmevServerService getService() {
		SmevServerService srv = service;
		if (srv == null) {
			srv = new SmevServerService();
			service = srv;
		}
		return service;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.SmevClient#asyncRequest(com.agiliumlabs.smev.ws.model.SmevData)
	 */
	@Override
	public SmevData asyncRequest(SmevData message) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.SmevClient#asyncResponse(com.agiliumlabs.smev.ws.model.SmevData)
	 */
	@Override
	public SmevData asyncResponse(SmevData message) {
		// TODO Auto-generated method stub
		return null;
	}

}
