/**
 * 
 */
package com.agiliumlabs.smev.ws.sample;

import javax.inject.Inject;

import com.agiliumlabs.smev.ws.Client;
import com.agiliumlabs.smev.ws.RoutingRule;
import com.agiliumlabs.smev.ws.SmevAgent;
import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
@Client
public class SmevTestClientRoutingRule implements RoutingRule {

	@Inject @Client
	private SmevTestClient route;
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.RoutingRule#selectRoute(com.agiliumlabs.smev.ws.model.SmevData, java.lang.Iterable)
	 */
	@Override
	public SmevAgent selectRoute(SmevData message, Iterable<SmevAgent> routes) {
		if ("serviceCode".equals(message.getMessage().getServiceCode()))
			return route;
		return null;
	}

}
