package ch.ehi.iox.objpool.impl;

import java.io.IOException;

public class LongSerializer implements Serializer<Long> {
	@Override
	public byte[] getBytes(Long value) throws IOException {
		byte[] b=new byte[8];
		longToBytes(value,b);
		return b;
	}

	@Override
	public Long getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		return bytesToLong(bytes);
	}

	public final static long bytesToLong( byte[] b )
	{
		assert b.length >= 8;
		return ( ( b[0] & 0xFFL ) << 56 ) + ( ( b[1] & 0xFFL ) << 48 )
				+ ( ( b[2] & 0xFFL ) << 40 ) + ( ( b[3] & 0xFFL ) << 32 )
				+ ( ( b[4] & 0xFFL ) << 24 ) + ( ( b[5] & 0xFFL ) << 16 )
				+ ( ( b[6] & 0xFFL ) << 8 ) + ( ( b[7] & 0xFFL ) << 0 );

	}
	public final static void longToBytes( long v, byte[] b )
	{
		assert b.length >= 8;
		b[0] = (byte) ( ( v >>> 56 ) & 0xFF );
		b[1] = (byte) ( ( v >>> 48 ) & 0xFF );
		b[2] = (byte) ( ( v >>> 40 ) & 0xFF );
		b[3] = (byte) ( ( v >>> 32 ) & 0xFF );
		b[4] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[5] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[6] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[7] = (byte) ( ( v >>> 0 ) & 0xFF );
	}
	public final static int bytesToInteger( byte[] b,int offset )
	{
		assert b.length >= offset+4;
		return ( ( b[offset+0] & 0xFF ) << 24 ) + ( ( b[offset+1] & 0xFF ) << 16 )
				+ ( ( b[offset+2] & 0xFF ) << 8 ) + ( ( b[offset+3] & 0xFF ) << 0 );
	}
	public final static void integerToBytes( int v, byte[] b,int offset )
	{
		assert b.length >= offset+4;
		b[0+offset] = (byte) ( ( v >>> 24 ) & 0xFF );
		b[1+offset] = (byte) ( ( v >>> 16 ) & 0xFF );
		b[2+offset] = (byte) ( ( v >>> 8 ) & 0xFF );
		b[3+offset] = (byte) ( ( v >>> 0 ) & 0xFF );
	}

	public final static void doubleToBytes( double  value, byte[] b )
	{
		long v = Double.doubleToRawLongBits( (Double) value );
		longToBytes(v, b);
	}
	public final static double bytesToDouble( byte[] b )
	{
		long v=bytesToLong(b);
		return Double
				.longBitsToDouble( v );
	}

}
