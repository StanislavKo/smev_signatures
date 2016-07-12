/**
 * 
 */
package com.agiliumlabs.smev.ws;

import java.util.List;

import javax.xml.ws.handler.Handler;

import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
public interface SmevAgent {

	public SmevData syncRequest(List<Handler> handlerList, SmevData message);
	public SmevData asyncRequest(SmevData message);
	public SmevData asyncResponse(SmevData message);
	
}
