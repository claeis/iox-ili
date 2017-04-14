package ch.interlis.iox_j.validator;

public class PlausibilityPoolValue {
	
	private double successfulResults;
	private double totalSumOfConstraints;

	@SuppressWarnings("unused")
	private PlausibilityPoolValue(){
	}
	
	protected PlausibilityPoolValue(double successfulResults, double totalSumOfConstraints) {
		this.setSuccessfulResults(successfulResults);
		this.setTotalSumOfConstraints(totalSumOfConstraints);
	}

	protected double getSuccessfulResults() {
		return successfulResults;
	}

	protected void setSuccessfulResults(double successfulResults){
		this.successfulResults = successfulResults;
	}

	protected double getTotalSumOfConstraints() {
		return totalSumOfConstraints;
	}

	protected void setTotalSumOfConstraints(double totalSumOfConstraints) {
		this.totalSumOfConstraints = totalSumOfConstraints;
	}
}