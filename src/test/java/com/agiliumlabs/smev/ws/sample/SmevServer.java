
package com.agiliumlabs.smev.ws.sample;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.soap.MTOM;

import ru.gosuslugi.smev.rev120315.HeaderType;
import ru.gosuslugi.smev.rev120315.SyncRequestType;
import ru.gosuslugi.smev.rev120315.SyncResponseType;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6b21 
 * Generated source version: 2.0
 * 
 */
@WebService(name = "SmevServer", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
//@XmlSeeAlso(value = {org.w3._2004._08.xop.include.ObjectFactory.class, TestData.class})
@MTOM(threshold=5*1024*1024)
public interface SmevServer {


    /**
     * 
     * @param smevHeader
     * @param syncReqParameters
     * @return
     *     returns com.agiliumlabs.smev.ws.impl.server.smevserver.xsd.types.SyncResponseType
     */
    @WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/syncReq")
    @WebResult(name = "SyncResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "syncReqResponseParameters")
    public SyncResponseType syncReq(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "SyncRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "syncReqParameters")
        SyncRequestType syncReqParameters);

    /**
     * 
     * @param smevHeader
     * @param asyncReqParameters
     * @return
     *     returns com.agiliumlabs.smev.ws.impl.server.smevserver.xsd.types.SyncResponseType
     */
    @WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/aSyncReq")
    @WebResult(name = "aSyncReqResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "asyncReqResponseParameters")
    public SyncResponseType aSyncReq(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "aSyncReqRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "asyncReqParameters")
        SyncRequestType asyncReqParameters);

    /**
     * 
     * @param smevHeader
     * @param aSyncResultParameters
     * @return
     *     returns com.agiliumlabs.smev.ws.impl.server.smevserver.xsd.types.SyncResponseType
     */
    @WebMethod(action = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/aSyncResult")
    @WebResult(name = "aSyncResultResponse", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "aSyncResultResponseParameters")
    public SyncResponseType aSyncResult(
        @WebParam(name = "Header", targetNamespace = "http://smev.gosuslugi.ru/rev120315", header = true, partName = "smevHeader")
        HeaderType smevHeader,
        @WebParam(name = "aSyncResultRequest", targetNamespace = "http://server.impl.ws.smev.agiliumlabs.com/SmevServer/xsd/types", partName = "aSyncResultParameters")
        SyncRequestType aSyncResultParameters);

}
