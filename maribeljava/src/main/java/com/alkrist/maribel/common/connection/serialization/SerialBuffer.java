package com.alkrist.maribel.common.connection.serialization;

import java.util.Arrays;

import com.alkrist.maribel.utils.ByteConverter;

/**
 * The SerialBuffer class stores the byte array af the given capacity and handles
 * the data deposit/withdraw from the buffer. Also has the methods to convert different 
 * data types to bytes and back.
 * 
 * @author Mikhail
 *
 */
public class SerialBuffer {

	private static final byte TRUE = 1;
	private static final byte FALSE = 0;
	
	private int capacity = 1024 * 1024; //1MB
	
	public byte[] data = new byte[capacity];
	public int pos; //A pointer to the position in byte array
	public int length;
	
	private void checkCapacity(int add) {
		if((pos+add)>= capacity) {
			capacity <<= 1;
			data = Arrays.copyOf(data, capacity);
		}
	}
	
	public byte[] toArray() {
		return Arrays.copyOf(data, pos);
	}
	
	public int sizeLeft() {
		return capacity - pos;
	}
	
	public void reset() {
		pos = 0;
		length = 0;
	}
	
	public void rewind() {
		length = pos;
		pos = 0;
	}
	
	
	/******* WRITE DATA TO BUFFER *******/
	
	/**
	 * Writes a 1 byte value to the buffer.
	 * 
	 * @param value - 1 byte value
	 */
	public void writeByte(byte value) {
		checkCapacity(1);
		data[pos++]=value;
	}
	
	/**
	 * Writes a boolean value as a 1 byte value
	 * to the buffer.
	 * 
	 * @param value - boolean value
	 */
	public void writeBoolean(boolean value) {
		writeByte(value ? TRUE : FALSE);
	}
	
	/**
	 * Writes the array of bytes to the buffer.
	 * 
	 * @param value - byte array
	 */
	public void writeByteArray(byte[] value) {
		int length = value.length;
		checkCapacity(length);
		System.arraycopy(value, 0, data, pos, length);
		pos+=length;
	}
	
	/**
	 * Writes an integer (4 bytes) value to the buffer.
	 * 
	 * @param value - integer value
	 */
	public void writeInt(int value) {
		checkCapacity(4);
		ByteConverter.setInt32(data, pos, value);
		pos+=4;
	}
	
	/**
	 * Writes a long (8 bytes) value to the buffer.
	 * 
	 * @param value - long value
	 */
	public void writeLong(long value) {
		checkCapacity(8);
		ByteConverter.setInt64(data, pos, value);
		pos+=8;
	}
	
	/**
	 * Writes a short (2 bytes) value to the buffer.
	 * 
	 * @param value - short value
	 */
	public void writeShort(short value) {
		checkCapacity(2);
		ByteConverter.setInt16(data, pos, value);
		pos+=2;
	}
	
	/**
	 * Writes a floating point (4 bytes) value to the buffer.
	 * 
	 * @param value - float value
	 */
	public void writeFloat(float value) {
		checkCapacity(4);
		ByteConverter.setDouble(data, pos, value);
		pos+=4;
	}
	
	/**
	 * Writes a floating double precise (8 byte) value to the buffer.
	 * 
	 * @param value - double value
	 */
	public void writeDouble(double value) {
		checkCapacity(8);
		ByteConverter.setDouble(data, pos, value);
		pos+=8;
	}
	
	/**
	 * Writes a character (2 bytes) value to the buffer.
	 * 
	 * @param value - char value
	 */
	public void writeChar(char value) {
		checkCapacity(2);
		ByteConverter.setInt16(data, pos, value);
		pos+=2;
	}
	
	/**
	 * Writes a string value as an array of chars (2 bytes each) to the buffer.
	 * 
	 * @param value - String value
	 */
	public void writeString(String value) {
		char[] chars = value.toCharArray();
		int length = chars.length;
		checkCapacity(length*2);
		for(int i=0; i<length; i++) {
			ByteConverter.setInt16(data, pos, chars[i]);
			pos+=2;
		}		
	}
	
	
	/******* PEEK DADA *******/
	
	/**
	 * @return 1 byte from buffer at current position.
	 */
	public byte peekByte() {
		return data[pos];
	}
	
	/**
	 * @param offset - position offset in the buffer
	 * @return 1 byte from buffer at current position + offset.
	 */
	public byte peekByte(int offset) {
		return data[pos+offset];
	}
	
	/**
	 * @return whether the current element in buffer isn't 0.
	 */
	public boolean peekBoolean(){
		return data[pos] != 0;
	}
	
	/**
	 * @param offset - position offset in the buffer
	 * @return whether the current element in buffer + offset isn't 0.
	 */
	public boolean peekBoolean(int offset) {
		return data[pos+offset] != 0;
	}
	
	/**
	 * @return integer (4 bytes) value at current position.
	 */
	public int peekInt() {
		return ByteConverter.getInt32(data, pos);
	}
	
	/**
	 * @param offset - position offset in the buffer
	 * @return integer (4 bytes) value at current position + offset.
	 */
	public int peekInt(int offset) {
		return ByteConverter.getInt32(data, pos+offset);
	}
	
	
	/******* READ DATA FROM BUFFER *******/
	
	/**
	 * @return 1 byte value from current position.
	 */
	public byte readByte() {
		return data[pos++];
	}
	
	/**
	 * @return boolean value from current position
	 */
	public boolean readBoolean() {
		return readByte() == TRUE;
	}
	
	/**
	 * Copies the byte data from buffer to the given array
	 * 
	 * @param arr - destination array
	 * @return length of the destionation array
	 */
	public int readByteArray(byte[] arr) {
		int length = arr.length;
		System.arraycopy(data, pos, arr, 0, length);
		pos+=length;
		return length;
	}
	
	/**
	 * @return integer value from current position
	 */
	public int readInt() {
		int r = (int) ByteConverter.getInt32(data, pos);
		pos+=4;
		return r;
	}
	
	/**
	 * @return long value from current position
	 */
	public long readLong() {
		long r = (long) ByteConverter.getInt64(data, pos);
		pos+=8;
		return r;
	}
	
	/**
	 * @return short value from current position
	 */
	public short readShort() {
		short r = (short) ByteConverter.getInt16(data, pos);
		pos+=2;
		return r;
	}
	
	/**
	 * @return float value from current position
	 */
	public float readFloat() {
		float r = ByteConverter.getFloat(data, pos);
		pos+=4;
		return r;
	}
	
	/**
	 * @return double value from current position
	 */
	public double readDouble() {
		double r = ByteConverter.getDouble(data, pos);
		pos+=8;
		return r;
	}
	
	/**
	 * @return char value from current position
	 */
	public char readChar() {
		char r = (char) ByteConverter.getInt16(data, pos);
		pos+=2;
		return r;
	}
	
	/**
	 * Reads a string from a buffer from current position with the length given.
	 * 
	 * @param length - string length
	 * @return string value
	 */
	public String readString(int length) {
		char[] chars = new char[length];
		for(int i=0; i<length; i++) {
			chars[i] = (char) ByteConverter.getInt16(data, pos);
			pos+=2;
		}
		return String.valueOf(chars);
	}
}

