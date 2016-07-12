/**
 * 
 */
package com.agiliumlabs.smev.ws.sample;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.ws.handler.Handler;

import com.agiliumlabs.smev.ws.Endpoint;
import com.agiliumlabs.smev.ws.Server;
import com.agiliumlabs.smev.ws.SmevAgent;
import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
@Server
@Endpoint
@ApplicationScoped
public class SmevTestServerReceiver implements SmevAgent {

	private SmevData message;
	
	public SmevData getMessage() {
		return message;
	}
	
	@Override
	public SmevData syncRequest(List<Handler> handlerList, SmevData message) {
		this.message = message;
		return message;
	}

	@Override
	public SmevData asyncRequest(SmevData message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmevData asyncResponse(SmevData message) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
