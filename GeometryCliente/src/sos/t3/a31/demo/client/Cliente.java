package sos.t3.a31.demo.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import sos.t3.a31.demo.client.GeometryServiceStub.CircleArea;
import sos.t3.a31.demo.client.GeometryServiceStub.CircleAreaResponse;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GeometryServiceStub st = new GeometryServiceStub();
			CircleArea param = new CircleArea();
			param.setRadius(2.0);
			CircleAreaResponse res = st.circleArea(param);
			System.out.println(res.get_return());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
