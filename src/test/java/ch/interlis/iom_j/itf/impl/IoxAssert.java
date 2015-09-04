package ch.interlis.iom_j.itf.impl;

import org.junit.ComparisonFailure;

public class IoxAssert {
	private IoxAssert(){}

	public static void assertStartsWith(String expected,String actual)
	{
		if(expected==null && actual==null){
			return;
		}
		if(actual==null || expected==null || !actual.startsWith(expected)){
			throw new ComparisonFailure("",expected,actual);
		}
		
	}
	
}
