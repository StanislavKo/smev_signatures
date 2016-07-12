/**
 * 
 */
package com.agiliumlabs.smev.ws;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.xml.ws.handler.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiliumlabs.smev.ws.model.SmevData;

/**
 * @author roman
 *
 */
public abstract class Router implements SmevAgent {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	protected Instance<SmevAgent> routes;
	protected Instance<RoutingRule> routingRules;
	
	public abstract void setRoutes(Instance<SmevAgent> routes);
	public abstract void setRoutingRules(Instance<RoutingRule> routingRules);
		
	/* (non-Javadoc)
	 * @see com.agiliumlabs.smev.ws.SmevClient#syncRequest(com.agiliumlabs.smev.ws.model.SmevData)
	 */
	@Override
	public SmevData syncRequest(List<Handler> handlerList, SmevData message) {
		SmevAgent route = selectRoute(message);
		System.out.println("Router.syncRequest()");
		return route.syncRequest(handlerList, message);
	}
	
	public SmevAgent selectRoute(SmevData message) {
		for (RoutingRule rule : routingRules) {
			SmevAgent route = rule.selectRoute(message, routes);
			if (route != null)
				return route;
		}
		log.warn("There is no route for {}", message);
		return null;
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
