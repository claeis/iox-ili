package ch.ehi.iox.objpool.impl;

import java.util.Comparator;


public class StringComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		return arg0.compareTo(arg1);
	}

}
