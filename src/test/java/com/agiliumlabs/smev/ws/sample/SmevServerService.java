
package com.agiliumlabs.smev.ws.sample;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6b21 
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "SmevServerService", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", wsdlLocation = "http://localhost:18080/smev?wsdl")
public class SmevServerService
    extends Service
{

    private final static URL SMEVSERVERSERVICE_WSDL_LOCATION;
    private final static WebServiceException SMEVSERVERSERVICE_EXCEPTION;
    private final static QName SMEVSERVERSERVICE_QNAME = new QName("http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", "SmevServerService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:18080/smev?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SMEVSERVERSERVICE_WSDL_LOCATION = url;
        SMEVSERVERSERVICE_EXCEPTION = e;
    }

    public SmevServerService() {
        super(__getWsdlLocation(), SMEVSERVERSERVICE_QNAME);
    }

    public SmevServerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns SmevServer
     */
    @WebEndpoint(name = "SmevServerPort")
    public SmevServer getSmevServerPort() {
        return super.getPort(new QName("http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", "SmevServerPort"), SmevServer.class);
    }

    private static URL __getWsdlLocation() {
        if (SMEVSERVERSERVICE_EXCEPTION!= null) {
            throw SMEVSERVERSERVICE_EXCEPTION;
        }
        return SMEVSERVERSERVICE_WSDL_LOCATION;
    }

}
