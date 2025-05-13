
/**
 * CalculatorServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package sos.t3.a32.calculator;
/**
 *  CalculatorServiceSkeleton java skeleton for the axisService
 */
public class CalculatorServiceSkeleton{

	static private int contadorStatic = 0;
	private int lastResult = 0;

	
	
	public Result add (SimpleAddition simpleAddition) throws ErrorInOperation{
		Result res = new Result();
		
		TSimpleAddition param = simpleAddition.getSimpleAddition();
		int valor1= param.getAddend1();
		int valor2= param.getAddend2();
		System.out.println("[ADD] contador inicial: "+lastResult);
		System.out.println("[ADD] valor1: " + valor1 + " - valor2: "+valor2);
		lastResult = valor1 + valor2;
		res.setResult(lastResult);
		contadorStatic= valor1 + valor2;
		System.out.println("[ADD] Resultado: "+lastResult);
		System.out.println("[ADD] contador final: "+lastResult);
		System.out.println("[ADD] contador STATIC: "+contadorStatic);

		return res;
	}

	public sos.t3.a32.calculator.Result addArray (sos.t3.a32.calculator.ArrayAddition arrayAddition) throws ErrorInOperation{
		Result res = new Result();
		System.out.println("[ADDARRAY] contador inicial: "+lastResult);
		TArrayAddition param = arrayAddition.getArrayAddition();
		int[] array = param.getAddend();
		int sum=0;
		for (int i : array) {
			sum+=i;
		}
		lastResult=sum;
		contadorStatic= sum;
		System.out.println("[ADDARRAY] RESULTADO: "+lastResult);
		System.out.println("[ADDARRAY] contador FINAL: "+lastResult);
		System.out.println("[ADDARRAY] contador STATIC: "+contadorStatic);

		res.setResult(lastResult);		
		return res;
	}

	public sos.t3.a32.calculator.Result incrementValue (sos.t3.a32.calculator.Increment increment) throws ErrorInOperation{
		Result res = new Result();
		System.out.println("[INC] contador inicial: "+lastResult);

		int valor = increment.getIncrement();
		lastResult+= valor;
		contadorStatic+= valor;
		res.setResult(lastResult);
		System.out.println("[INC] contador FINAL: "+lastResult);
		System.out.println("[INC] contador STATIC: "+contadorStatic);
		return res;
	}

}
