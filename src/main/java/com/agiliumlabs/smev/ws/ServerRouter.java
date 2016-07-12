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
@Server
public class ServerRouter extends Router {

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.Router#setRoutes(javax.enterprise.inject.Instance)
	 */
	@Inject
	@Override
	public void setRoutes(@Server @Endpoint Instance<SmevAgent> routes) {
		this.routes = routes;
	}

	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.Router#setRoutingRules(javax.enterprise.inject.Instance)
	 */
	@Inject
	@Override
	public void setRoutingRules(@Server Instance<RoutingRule> routingRules) {
		this.routingRules = routingRules;
	}

}
