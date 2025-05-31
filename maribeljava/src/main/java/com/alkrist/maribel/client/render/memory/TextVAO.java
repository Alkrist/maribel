package com.alkrist.maribel.client.render.memory;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import com.alkrist.maribel.client.components.ui.fonts.FontMeshData;
import com.alkrist.maribel.client.util.Util;

public class TextVAO implements VBO{

	private int vaoId;
	private int positionsVbo;
	private int textureCoordsVbo;
	
	private int vertexCount;
	
	public TextVAO() {
		vaoId = glGenVertexArrays();
		positionsVbo = glGenBuffers();
		textureCoordsVbo = glGenBuffers();
	}
	
	public void addData(FontMeshData mesh) {
		glBindVertexArray(vaoId);
		
		glBindBuffer(GL_ARRAY_BUFFER, positionsVbo);
		FloatBuffer positionsBuffer = Util.createFlippedBuffer(mesh.getVertexPositions());
		glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW); // or dynamic???
		
		glBindBuffer(GL_ARRAY_BUFFER, textureCoordsVbo);
		FloatBuffer textureCoordsBuffer = Util.createFlippedBuffer(mesh.getTextureCoords());
		glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		this.vertexCount = mesh.getVertexCount();
	}
	
	
	@Override
	public void draw() {
		glBindVertexArray(vaoId);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
	}

	@Override
	public void delete() {
		glBindVertexArray(vaoId);
		
		glDeleteBuffers(positionsVbo);
		glDeleteBuffers(textureCoordsVbo);
		
		glDeleteVertexArrays(vaoId);
		
		glBindVertexArray(0);
	}

}
