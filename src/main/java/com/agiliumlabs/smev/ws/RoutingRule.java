/**
 * 
 */
package com.agiliumlabs.smev.ws;

import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
public interface RoutingRule {

	public SmevAgent selectRoute(SmevData message, Iterable<SmevAgent> routes);
	
}
