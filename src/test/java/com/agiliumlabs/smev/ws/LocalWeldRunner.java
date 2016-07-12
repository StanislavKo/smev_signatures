/**
 * 
 */
package com.agiliumlabs.smev.ws;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author roman
 *
 */
public class LocalWeldRunner extends BlockJUnit4ClassRunner {

	private WeldContainer container;
	private Class<?> klass;
	
	public LocalWeldRunner(Class<?> klass) throws InitializationError {
		super(klass);
		this.klass = klass;
	}

	@Override
	public void run(RunNotifier run) {
		container = new Weld().initialize();
		super.run(run);
	}

	@Override
    protected Object createTest() throws Exception {
        final Object test = container.instance().select(klass).get(); 
        return test;
    }
}
