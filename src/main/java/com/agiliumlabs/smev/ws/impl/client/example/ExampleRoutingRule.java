/**
 * 
 */
package com.agiliumlabs.smev.ws.impl.client.example;

import javax.inject.Inject;

import com.agiliumlabs.smev.ws.Client;
import com.agiliumlabs.smev.ws.RoutingRule;
import com.agiliumlabs.smev.ws.SmevAgent;
import com.agiliumlabs.smev.ws.model.Message;
import com.agiliumlabs.smev.ws.model.Organization;
import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
@Client
public class ExampleRoutingRule implements RoutingRule {

	private static final String EXAMPLE = "EXAMPLE";
	@Inject @Client
	private ExampleClient route;
	
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.RoutingRule#selectRoute(com.agiliumlabs.smev.ws.model.SmevData, java.lang.Iterable)
	 */
	@Override
	public SmevAgent selectRoute(SmevData message, Iterable<SmevAgent> routes) {
		if (message != null) {
			Message msg = message.getMessage();
			if (msg != null) {
				Organization org = msg.getRecepient();
				if (org != null) 
					if (EXAMPLE.equals(org.getCode()))
						return route;
			}
		}
		return null;
	}

}
