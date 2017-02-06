package ch.ehi.iox.objpool.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializer<K> implements Serializer<K>
{

	public byte[] getBytes( K object ) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		ObjectOutputStream oo = new ObjectOutputStream( out );
		oo.writeObject( object );
		return out.toByteArray( );
	}

	public K getObject( byte[] bytes ) throws IOException,
			ClassNotFoundException
	{
		ByteArrayInputStream in = new ByteArrayInputStream( bytes );
		ObjectInputStream oi = new ObjectInputStream( in );
		return (K) oi.readObject( );
	}
}
