package com.alkrist.maribel.utils;

/**
 * Used for converting values of different types to bytes and vise versa,
 * writing them to byte array and reading from byte array.
 * 
 * Originally found in Javaforce library.
 * 
 * @author Mikhail
 *
 */
public class ByteConverter {

/**
 * Get an integer 1 byte value from byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @return integer value
 */
public static int getInt8(byte[] data, int pos) {
	int r;
	
	r = (int)data[pos] & 0xff;
	return r;
}

/**
 * Get an integer 2 bytes value from byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @return integer value
 */
public static int getInt16(byte[] data, int pos) {
	int r;
	
	r = (int)data[pos] & 0xff;
	r+= ((int)data[pos+1] & 0xff) << 8;
	
	return r;
}

/**
 * Get an integer 4 bytes value from byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @return integer value
 */
public static int getInt32(byte[] data, int pos) {
	int r;
	
	r = (int)data[pos] & 0xff;
	r+= ((int)data[pos+1] & 0xff) << 8;
	r+= ((int)data[pos+2] & 0xff) << 16;
	r+= ((int)data[pos+3] & 0xff) << 24;
	
	return r;
}

/**
 * Get an integer 8 bytes value from byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @return long value
 */
public static int getInt64(byte[] data, int pos) {
	int r;
	
	r = (int)data[pos] & 0xff;
	r+= ((int)data[pos+1] & 0xff) << 8;
	r+= ((int)data[pos+2] & 0xff) << 16;
	r+= ((int)data[pos+3] & 0xff) << 24;
	r+= ((int)data[pos+4] & 0xff) << 32;
	r+= ((int)data[pos+5] & 0xff) << 40;
	r+= ((int)data[pos+6] & 0xff) << 48;
	r+= ((int)data[pos+7] & 0xff) << 56;
	
	return r;
}

/**
 * Get a floating point value (4 bytes) from byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @return float value
 */
public static float getFloat(byte[] data, int pos) {
	return Float.intBitsToFloat(getInt32(data, pos));
}

/**
 * Get a floating double precise value (8 bytes) from byte array
 * 
 * @param data
 * @param pos
 * @return double value
 */
public static double getDouble(byte[] data, int pos) {
	return Double.longBitsToDouble(getInt64(data, pos));
}

/**
 * Set an integer 1 byte value to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value integer value
 */
public static void setInt8(byte[] data, int pos, int value) {
	data[pos] = (byte)(value & 0xff);
}

/**
 * Set an integer 2 bytes value to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value  integer value
 */
public static void setInt16(byte[] data, int pos, int value) {
	data[pos] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+1] = (byte)(value & 0xff);
	
}

/**
 * Set an integer 4 bytes value to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value integer value
 */
public static void setInt32(byte[] data, int pos, int value) {
	data[pos] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+1] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+2] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+3] = (byte)(value & 0xff);
}

/**
 * Set an integer 8 bytes value to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value long value
 */
public static void setInt64(byte[] data, int pos, long value) {
	data[pos] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+1] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+2] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+3] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+4] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+5] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+6] = (byte)(value & 0xff);
	value >>= 8;
	data[pos+7] = (byte)(value & 0xff);
}

/**
 * Set a floating value (4 bytes) to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value float value
 */
public static void setFloat(byte[] data, int pos, float value) {
	setInt32(data, pos, Float.floatToIntBits(value));
}

/**
 * Set a floating double precise value (8 bytes) to byte array
 * 
 * @param data - byte array
 * @param pos - position in the array to start reading
 * @param value double value
 */
public static void setDouble(byte[] data, int pos, double value) {
	setInt64(data, pos, Double.doubleToLongBits(value));
}
}


