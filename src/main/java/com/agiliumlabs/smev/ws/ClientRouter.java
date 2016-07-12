/**
 * 
 */
package com.agiliumlabs.smev.ws;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author roman
 *
 */
@Facade
@Client
public class ClientRouter extends Router {

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.Router#setRoutes(javax.enterprise.inject.Instance)
	 */
	@Inject
	@Override
	public void setRoutes(@Client @Endpoint Instance<SmevAgent> routes) {
		this.routes = routes;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.Router#setRoutingRules(javax.enterprise.inject.Instance)
	 */
	@Inject
	@Override
	public void setRoutingRules(@Client Instance<RoutingRule> routingRules) {
		this.routingRules = routingRules;
	}

}
