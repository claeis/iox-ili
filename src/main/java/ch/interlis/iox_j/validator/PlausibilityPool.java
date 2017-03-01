package ch.interlis.iox_j.validator;

public class PlausibilityPool {
	
	private double resultTrue;
	private double resultTotal;

	public PlausibilityPool(){
	}
	
	public PlausibilityPool(double resultTrue, double resultTotal){
		this.setResultTrue(resultTrue);
		this.setResultTotal(resultTotal);
	}

	public double getResultTrue() {
		return resultTrue;
	}

	public void setResultTrue(double resultTrue) {
		this.resultTrue = resultTrue;
	}

	public double getResultTotal() {
		return resultTotal;
	}

	public void setResultTotal(double resultTotal) {
		this.resultTotal = resultTotal;
	}
}