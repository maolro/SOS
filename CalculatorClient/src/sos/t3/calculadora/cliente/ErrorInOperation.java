
/**
 * ErrorInOperation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package sos.t3.calculadora.cliente;

public class ErrorInOperation extends java.lang.Exception{

    private static final long serialVersionUID = 1746443495906L;
    
    private sos.t3.calculadora.cliente.CalculatorServiceStub.SomethingIsWrong faultMessage;

    
        public ErrorInOperation() {
            super("ErrorInOperation");
        }

        public ErrorInOperation(java.lang.String s) {
           super(s);
        }

        public ErrorInOperation(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ErrorInOperation(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(sos.t3.calculadora.cliente.CalculatorServiceStub.SomethingIsWrong msg){
       faultMessage = msg;
    }
    
    public sos.t3.calculadora.cliente.CalculatorServiceStub.SomethingIsWrong getFaultMessage(){
       return faultMessage;
    }
}
    