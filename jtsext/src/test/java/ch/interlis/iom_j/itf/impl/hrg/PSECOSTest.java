package ch.interlis.iom_j.itf.impl.hrg;

import static org.junit.Assert.*;

import org.junit.Test;

public class PSECOSTest {

	@Test
	public void testPSECOS() {
		double start_x=5;
		double start_y=0;
		double z_x=0;
		double z_y=0;
		double end_x[]={5, 0, -5,  0, 5};
		double end_y[]={0, 5,  0, -5, 0};
		//PSECOS hat linksdrehende Winkel
		for(int i=0;i<end_x.length;i++){
			double alpha =  HrgUtility.PSECOS (start_x,start_y,z_x,z_y,end_x[i],end_y[i]); 
			System.out.println("x "+end_x[i]+",y "+end_y[i]+": "+alpha);
			
		}
	}

}
