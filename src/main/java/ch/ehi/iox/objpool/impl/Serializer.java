package ch.ehi.iox.objpool.impl;

import java.io.IOException;

public interface Serializer<K>
{

	byte[] getBytes( K object ) throws IOException;

	K getObject( byte[] bytes ) throws IOException, ClassNotFoundException;

}
