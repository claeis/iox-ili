package ch.ehi.iox.objpool.impl;

import java.io.Serializable;
import java.util.Comparator;

public class JavaComparator<K> implements Comparator<K>, Serializable
{
	private static final long serialVersionUID = -2374485501916583296L;

	public int compare( K o1, K o2 )
	{
		if ( o1 == null )
		{
			if ( o2 == null )
			{
				return 0;
			}
			return -1;
		}
		if ( o2 == null )
		{
			return 1;
		}
		if ( o1 instanceof Comparable )
		{
			return ( (Comparable) o1 ).compareTo( o2 );
		}
		if ( o2 instanceof Comparable )
		{
			return -( (Comparable) o2 ).compareTo( o1 );
		}
		return 0;
	}
}
