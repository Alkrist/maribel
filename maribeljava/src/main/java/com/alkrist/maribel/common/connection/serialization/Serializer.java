package com.alkrist.maribel.common.connection.serialization;

import java.util.logging.Level;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.alkrist.maribel.utils.Logging;

/**
 * This class performs a zip compression of a {@link connection.serialization.Serializable} object 
 * and further decompression via the {@link connection.serialization.SerialBuilder} interface.
 * 
 * There're the following steps going on: Serialiazable object -> byte array -> compressed byte array ->
 * certain tasks are preformed here -> decompressed byte array -> Serializable object.
 * 
 * @author Mikhail
 *
 */
public class Serializer {

	private SerialBuffer objectBuffer;
	private SerialBuffer compressedBuffer;
	private SerialBuffer decompressBuffer;
	
	public Serializer() {
		objectBuffer = new SerialBuffer();
		compressedBuffer = new SerialBuffer();
		decompressBuffer = new SerialBuffer();
	}
	
	/**
	 * Convert {@link connection.serialization.Serializable} object into compressed byte array.
	 * 
	 * @param obj - Serializable object
	 * @return compressed byte array
	 */
	public synchronized byte[] encode(Serializable obj) {
		try {
			
			//Prepare buffer
			objectBuffer.reset();
			if(!obj.write(objectBuffer)) return null; //write object to the object buffer
			Deflater compressor = new Deflater();
			compressor.setInput(objectBuffer.data, 0, objectBuffer.pos);
			compressor.finish();
			
			int bufferSize;
			
			//Compress and write to compressed buffer
			compressedBuffer.reset();
			do {
				
				bufferSize = compressor.deflate(compressedBuffer.data, compressedBuffer.pos, 
						compressedBuffer.sizeLeft());
				if(bufferSize == 0) break;
				compressedBuffer.pos = bufferSize;
				
			}while(true);
			
			if(compressedBuffer.pos == 0) return null; //Means that zipped buffer is empty
			return compressedBuffer.toArray();
			
		}catch(Exception e) {
			Logging.getLogger().log(Level.SEVERE, "An error occured durning encode", e);
			return null;
		}
	}
	
	/**
	 * Decompress byte array and decode in into {@link connection.serialization.Serializable} object.
	 * 
	 * @param data - compressed byte array
	 * @param builder - {@link connection.serialization.SerialBuilder} object
	 * @return object with implemented Serializable iface.
	 */
	public synchronized Object decode(byte[] data, SerialBuilder builder) {
		try {
			
			//Start decompressing data from compressed byte array
			Inflater decompressor = new Inflater();
			decompressor.setInput(data);
			
			int bufferSize;
			decompressBuffer.reset();
			
			do {
				
				bufferSize = decompressor.inflate(decompressBuffer.data, decompressBuffer.pos,
						decompressBuffer.sizeLeft());
				if(bufferSize == 0) break;
				decompressBuffer.pos += bufferSize;
							
			}while(true);
			
			//Trim the buffer and build an object with decompressed data
			decompressBuffer.length = decompressBuffer.pos;
			decompressBuffer.pos = 0;
			
			Serializable obj = (Serializable)builder.build(decompressBuffer);
			if(!obj.read(decompressBuffer)) return null; //Read the object from the buffer
			return obj;
			
		}catch(Exception e) {
			Logging.getLogger().log(Level.SEVERE, "An error occured durning decode", e);
			return null;
		}
	}
}

