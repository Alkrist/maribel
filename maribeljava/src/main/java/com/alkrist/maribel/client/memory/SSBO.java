package com.alkrist.maribel.client.memory;

import static org.lwjgl.opengl.GL15.GL_STATIC_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import com.alkrist.maribel.client.util.Util;

/**
 * Shader Storage Buffer Object
 * 
 * @author oreon-engine
 */
public class SSBO {

	private int ssbo;
	
	public SSBO(){
		ssbo = glGenBuffers();
	}
	
	public void addData(Vector2f[] data){
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		glBufferData(GL_SHADER_STORAGE_BUFFER, Util.createFlippedBuffer(data), GL_STATIC_READ);
	}
	
	public void addData(int[] data){
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		glBufferData(GL_SHADER_STORAGE_BUFFER, Util.createFlippedBuffer(data), GL_STATIC_READ);
	}
	
	public void addData(float[] data){
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		glBufferData(GL_SHADER_STORAGE_BUFFER, Util.createFlippedBuffer(data), GL_STATIC_READ);
	}
	
	public void addData(ByteBuffer data){
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo);
		glBufferData(GL_SHADER_STORAGE_BUFFER, data, GL_STATIC_READ);
	}
	
	public void bindBufferBase(int index){
		glBindBufferBase(GL_SHADER_STORAGE_BUFFER, index, ssbo);
	}
}
