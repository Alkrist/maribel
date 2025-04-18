package com.alkrist.maribel.client.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.client.model.Vertex;

public class Util {

	public static int[] toIntArray(List<Integer> data)
	{
		int[] result = new int[data.size()];
		
		for(int i=0; i < data.size(); i++)
			result[i] = data.get(i).intValue();
		
		return result;
	}
	
	public static Vertex[] toVertexArray(List<Vertex> data)
	{
		Vertex[] vertices = new Vertex[data.size()];
		
		for(int i=0; i<vertices.length; i++)
		{
			vertices[i] = new Vertex();
			vertices[i].setPosition(data.get(i).getPosition());
			vertices[i].setTextureCoord(data.get(i).getTextureCoord());
			vertices[i].setNormal(data.get(i).getNormal());
			vertices[i].setTangent(data.get(i).getTangent());
			vertices[i].setBitangent(data.get(i).getBitangent());
		}
		
		return vertices;
	}
	
	public static FloatBuffer createFlippedBufferAOS(Vertex[] vertices)
	{
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.FLOATS);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPosition().x);
			buffer.put(vertices[i].getPosition().y);
			buffer.put(vertices[i].getPosition().z);
			buffer.put(vertices[i].getNormal().x);
			buffer.put(vertices[i].getNormal().y);
			buffer.put(vertices[i].getNormal().z);
			buffer.put(vertices[i].getTextureCoord().x);
			buffer.put(vertices[i].getTextureCoord().y);
			
			if (vertices[i].getTangent() != null && vertices[i].getBitangent() != null){
				buffer.put(vertices[i].getTangent().x);
				buffer.put(vertices[i].getTangent().y);
				buffer.put(vertices[i].getTangent().z);
				buffer.put(vertices[i].getBitangent().x);
				buffer.put(vertices[i].getBitangent().y);
				buffer.put(vertices[i].getBitangent().z);
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFloatBuffer(int size){
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size){
		return BufferUtils.createIntBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values){
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(float... values){
		FloatBuffer buffer = createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector3f[] vector){
		FloatBuffer buffer = createFloatBuffer(vector.length * Float.BYTES * 3);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x);
			buffer.put(vector[i].y);
			buffer.put(vector[i].z);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector2f[] vector){
		FloatBuffer buffer = createFloatBuffer(vector.length * Float.BYTES * 2);
		
		for (int i = 0; i < vector.length; i++)
		{
			buffer.put(vector[i].x);
			buffer.put(vector[i].y);	
		}
		
		buffer.flip();
		
		return buffer;
	}
}
