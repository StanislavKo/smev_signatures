/**
 * 
 */
package com.agiliumlabs.smev.ws.impl.client.example;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;

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

/**
 * @author roman
 *
 */
@ApplicationScoped
@Client
@Endpoint
public class ExampleClient implements SmevAgent {

	@Inject
	private ExampleClientConfiguration config;
	@Inject
	private Rev120315Converter converter;
	private ThreadLocal<MsgExampleService> service = new ThreadLocal<MsgExampleService>();
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.SmevClient#syncRequest(com.agiliumlabs.smev.ws.model.SmevData)
	 */
	@Override
	public SmevData syncRequest(List<Handler> handlerList, SmevData message) {
		HeaderType header = converter.convertToSmev(message.getHeader());
		SyncRequestType params = convertRequest(message);
		SyncResponseType response = getService().getMsgExamplePort().syncReq(header, params);
		return convertResponse(header, response);
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
		System.out.println("\r\n\r\nRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR\r\n\r\n");
		SyncRequestType params = new SyncRequestType();
		MessageType msg = converter.convertToSmev(message.getMessage());
		params.setMessage(msg);
		ProcessCanonicalServiceMessageData data = converter.convertToSmev(message.getData());
		params.setMessageData(data);
		return params;
	}

	private MsgExampleService getService() {
		MsgExampleService srv = service.get();
		if (srv == null) {
			try {
				srv = new MsgExampleService(new URL(config.getWsdlUrl()), new QName("http://smev.gosuslugi.ru/MsgExample/", "MsgExampleService"));
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e);
			}
			service.set(srv);
		}
		return service.get();
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
