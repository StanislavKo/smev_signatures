/**
 * 
 */
package com.agiliumlabs.smev.ws.sample;

import javax.inject.Inject;

import com.agiliumlabs.smev.ws.RoutingRule;
import com.agiliumlabs.smev.ws.Server;
import com.agiliumlabs.smev.ws.SmevAgent;
import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
@Server
public class SmevTestServerRoutingRule implements RoutingRule {

	@Inject @Server
	private SmevTestServerReceiver receiver;
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.RoutingRule#selectRoute(com.agiliumlabs.smev.ws.model.SmevData, java.lang.Iterable)
	 */
	@Override
	public SmevAgent selectRoute(SmevData message, Iterable<SmevAgent> routes) {
		if ("serviceCode".equals(message.getMessage().getServiceCode()))
			return receiver;
		return null;
	}

}
