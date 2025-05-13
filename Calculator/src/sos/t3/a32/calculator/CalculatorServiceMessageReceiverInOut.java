
/**
 * CalculatorServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
        package sos.t3.a32.calculator;

        /**
        *  CalculatorServiceMessageReceiverInOut message receiver
        */

        public class CalculatorServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        CalculatorServiceSkeleton skel = (CalculatorServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){


        

            if("add".equals(methodName)){
                
                sos.t3.a32.calculator.Result result15 = null;
	                        sos.t3.a32.calculator.SimpleAddition wrappedParam =
                                                             (sos.t3.a32.calculator.SimpleAddition)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    sos.t3.a32.calculator.SimpleAddition.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               result15 =
                                                   
                                                   
                                                         skel.add(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), result15, false, new javax.xml.namespace.QName("http://calculator.a32.t3.sos",
                                                    "add"));
                                    } else 

            if("addArray".equals(methodName)){
                
                sos.t3.a32.calculator.Result result17 = null;
	                        sos.t3.a32.calculator.ArrayAddition wrappedParam =
                                                             (sos.t3.a32.calculator.ArrayAddition)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    sos.t3.a32.calculator.ArrayAddition.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               result17 =
                                                   
                                                   
                                                         skel.addArray(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), result17, false, new javax.xml.namespace.QName("http://calculator.a32.t3.sos",
                                                    "addArray"));
                                    } else 

            if("incrementValue".equals(methodName)){
                
                sos.t3.a32.calculator.Result result19 = null;
	                        sos.t3.a32.calculator.Increment wrappedParam =
                                                             (sos.t3.a32.calculator.Increment)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    sos.t3.a32.calculator.Increment.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               result19 =
                                                   
                                                   
                                                         skel.incrementValue(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), result19, false, new javax.xml.namespace.QName("http://calculator.a32.t3.sos",
                                                    "incrementValue"));
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        } catch (ErrorInOperation e) {

            msgContext.setProperty(org.apache.axis2.Constants.FAULT_NAME,"somethingIsWrong");
            org.apache.axis2.AxisFault f = createAxisFault(e);
            if (e.getFaultMessage() != null){
                f.setDetail(toOM(e.getFaultMessage(),false));
            }
            throw f;
            }
        
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(sos.t3.a32.calculator.SimpleAddition param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(sos.t3.a32.calculator.SimpleAddition.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(sos.t3.a32.calculator.Result param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(sos.t3.a32.calculator.Result.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(sos.t3.a32.calculator.SomethingIsWrong param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(sos.t3.a32.calculator.SomethingIsWrong.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(sos.t3.a32.calculator.ArrayAddition param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(sos.t3.a32.calculator.ArrayAddition.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(sos.t3.a32.calculator.Increment param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(sos.t3.a32.calculator.Increment.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, sos.t3.a32.calculator.Result param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(sos.t3.a32.calculator.Result.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private sos.t3.a32.calculator.Result wrapadd(){
                                sos.t3.a32.calculator.Result wrappedElement = new sos.t3.a32.calculator.Result();
                                return wrappedElement;
                         }
                    
                         private sos.t3.a32.calculator.Result wrapaddArray(){
                                sos.t3.a32.calculator.Result wrappedElement = new sos.t3.a32.calculator.Result();
                                return wrappedElement;
                         }
                    
                         private sos.t3.a32.calculator.Result wrapincrementValue(){
                                sos.t3.a32.calculator.Result wrappedElement = new sos.t3.a32.calculator.Result();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (sos.t3.a32.calculator.SimpleAddition.class.equals(type)){
                
                           return sos.t3.a32.calculator.SimpleAddition.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.Result.class.equals(type)){
                
                           return sos.t3.a32.calculator.Result.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.SomethingIsWrong.class.equals(type)){
                
                           return sos.t3.a32.calculator.SomethingIsWrong.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.ArrayAddition.class.equals(type)){
                
                           return sos.t3.a32.calculator.ArrayAddition.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.Result.class.equals(type)){
                
                           return sos.t3.a32.calculator.Result.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.SomethingIsWrong.class.equals(type)){
                
                           return sos.t3.a32.calculator.SomethingIsWrong.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.Increment.class.equals(type)){
                
                           return sos.t3.a32.calculator.Increment.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.Result.class.equals(type)){
                
                           return sos.t3.a32.calculator.Result.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (sos.t3.a32.calculator.SomethingIsWrong.class.equals(type)){
                
                           return sos.t3.a32.calculator.SomethingIsWrong.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    