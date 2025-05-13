
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package sos.t3.a32.calculator;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://calculator.a32.t3.sos".equals(namespaceURI) &&
                  "tArrayAddition".equals(typeName)){
                   
                            return  sos.t3.a32.calculator.TArrayAddition.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://calculator.a32.t3.sos".equals(namespaceURI) &&
                  "tSimpleAddition".equals(typeName)){
                   
                            return  sos.t3.a32.calculator.TSimpleAddition.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    