package com.alkrist.maribel.client.graphics.loaders;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import com.alkrist.maribel.client.graphics.model.Mesh;


public class ResourceLoader {

	private static List<Integer> VAOs = new ArrayList<Integer>();
	private static List<Integer> VBOs = new ArrayList<Integer>();
	
	public static Mesh loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {		
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new Mesh(vaoID, indices.length);		
	}
	
	public static Mesh loadToVAO(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {		
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, vertices);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		storeDataInAttributeList(3, 3, normals);
		unbindVAO();
		
		return new Mesh(vaoID, indices.length);		
	}
	
	public static Mesh loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new Mesh(vaoID, positions.length/dimensions);
	}
	
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		VAOs.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	public static int createEmptyVBO(int floatCount) {
		int vbo = GL15.glGenBuffers();
		VBOs.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	/**
	 * Updates some data in VBO.
	 * 
	 * @param vbo - destination VBO
	 * @param data - data to update
	 * @param buffer - the buffer for multiple times use
	 */
	public static void updateVBO(int vbo, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Adds per-instance attribute to VAO
	 * 
	 * @param vao - the destination VAO
	 * @param vbo - the attribute is getting data from
	 * @param attribute - attribute number where we store data
	 * @param dataSize - the element size of each bit of data
	 * @param instancedDataLength - equals stride value of VBO
	 * @param offset - the data bit size in VBO
	 */
	public static void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		
		//Stride and Offset in bytes
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		GL33.glVertexAttribDivisor(attribute, 1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	private static void storeDataInAttributeList(int attribNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		VBOs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);		
		GL20.glVertexAttribPointer(attribNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		VBOs.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	public static void cleanUp() {
		for(int vao: VAOs)
			GL30.glDeleteVertexArrays(vao);
		for(int vbo: VBOs)
			GL15.glDeleteBuffers(vbo);
	}
}

