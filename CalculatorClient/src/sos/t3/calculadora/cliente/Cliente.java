package sos.t3.calculadora.cliente;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import sos.t3.calculadora.cliente.CalculatorServiceStub.ArrayAddition;
import sos.t3.calculadora.cliente.CalculatorServiceStub.Increment;
import sos.t3.calculadora.cliente.CalculatorServiceStub.Result;
import sos.t3.calculadora.cliente.CalculatorServiceStub.SimpleAddition;
import sos.t3.calculadora.cliente.CalculatorServiceStub.TArrayAddition;
import sos.t3.calculadora.cliente.CalculatorServiceStub.TSimpleAddition;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			CalculatorServiceStub st = new CalculatorServiceStub();
			st._getServiceClient().getOptions().setManageSession(true);
			//st._getServiceClient().engageModule("addressing");
			SimpleAddition param = new SimpleAddition();
			
			TSimpleAddition tParam = new TSimpleAddition();
			tParam.setAddend1(5);
			tParam.setAddend2(4);
			param.setSimpleAddition(tParam);
			Result res = st.add(param);
			System.out.println("El resultado de la suma es: " + res.getResult());
			
			ArrayAddition arrayParam = new ArrayAddition();
			TArrayAddition tArrayParam = new TArrayAddition();
			int[] array = {3,6,9};
			tArrayParam.setAddend(array);
			arrayParam.setArrayAddition(tArrayParam);
			res = st.addArray(arrayParam);
			System.out.println("el resultado de la suma de array es: "+ res.getResult());
			
			Increment inc = new Increment();
			inc.setIncrement(10);
			res = st.incrementValue(inc);
			System.out.println("El resultado después de incrementar 10 es: "+ res.getResult());
			
			inc.setIncrement(5);
			res = st.incrementValue(inc);
			System.out.println("El resultado después de incrementar 5 es: "+ res.getResult());
			
			CalculatorServiceStub st2 = new CalculatorServiceStub();
			st2._getServiceClient().getOptions().setManageSession(true);
			//st2._getServiceClient().engageModule("addressing");
			inc = new Increment();
			inc.setIncrement(10);
			res = st2.incrementValue(inc);
			System.out.println("USER_B: El resultado después de incrementar 10 es: "+ res.getResult());
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorInOperation e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
