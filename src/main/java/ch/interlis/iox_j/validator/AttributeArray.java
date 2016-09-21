package ch.interlis.iox_j.validator;
import java.util.ArrayList;
import java.util.Arrays;

public class AttributeArray {
	private String[] values;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(values);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeArray other = (AttributeArray) obj;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}
	public AttributeArray(ArrayList<String> values){
		this.values = values.toArray(new String[values.size()]);
	}
	private String[] getValues() {
		return values;
	}
	public String valuesAsString() {
		StringBuilder ret=new StringBuilder();
		String sep="";
		for(String v: values){
			ret.append(sep);
			ret.append(v);
			sep=", ";
		}
		return ret.toString();
	}
}
