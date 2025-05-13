package sos.t3.a32.calculator;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CalculatorServiceSkeleton cs = new CalculatorServiceSkeleton();
			Increment param = new Increment();
			param.setIncrement(10);
			Result res = cs.incrementValue(param);
			System.out.println("Resutado es: "+ res.getResult());
			
			param.setIncrement(5);
			res = cs.incrementValue(param);
			System.out.println("Resutado es: "+ res.getResult());
		} catch (ErrorInOperation e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
