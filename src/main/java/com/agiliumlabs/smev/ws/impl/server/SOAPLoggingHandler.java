/**
 * 
 */
package com.agiliumlabs.smev.ws.impl.server;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author roman
 *
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        log(smc);
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        log(smc);
        return true;
    }

    // nothing to clean up
    @Override
    public void close(MessageContext messageContext) {
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     */
    private void log(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean)
            smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage message = smc.getMessage();
        try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
            message.writeTo(bos);
            if (outboundProperty.booleanValue())
                log.debug("Outbound:\n" + new String(bos.toByteArray()));
            else 
                log.debug("Inbound:\n" + new String(bos.toByteArray()));

        } catch (Exception e) {
            log.warn("Exception in SOAPLoggerHandler", e);
        }
    }
}
