package com.alkrist.maribel.client.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.alkrist.maribel.client.render.model.Vertex;

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
	
	public static FloatBuffer createFlippedBufferAOS(Vertex[] vertices){
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
	
	public static FloatBuffer createFlippedBufferSOA(Vertex[] vertices){
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.FLOATS);
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getPosition().x);
			buffer.put(vertices[i].getPosition().y);
			buffer.put(vertices[i].getPosition().z);
		}
		
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getNormal().x);
			buffer.put(vertices[i].getNormal().y);
			buffer.put(vertices[i].getNormal().z);
		}
			
		for(int i = 0; i < vertices.length; i++)
		{
			buffer.put(vertices[i].getTextureCoord().x);
			buffer.put(vertices[i].getTextureCoord().y);
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
	
	public static FloatBuffer createFlippedBuffer(List<Vector2f> vector){
		FloatBuffer buffer = createFloatBuffer(vector.size() * Float.BYTES * 2);
		
		for (Vector2f v : vector)
		{
			buffer.put(v.x);
			buffer.put(v.y);	
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static int[] toIntArray(Integer[] data){
		int[] result = new int[data.length];
		
		for(int i=0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector3f vector) {
		FloatBuffer buffer = createFloatBuffer(Float.BYTES * 3);
		
		buffer.put(vector.x);
		buffer.put(vector.y);
		buffer.put(vector.z);
		
		buffer.flip();
		
		return buffer;
	}
}
