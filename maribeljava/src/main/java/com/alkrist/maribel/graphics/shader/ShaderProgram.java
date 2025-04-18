package com.alkrist.maribel.graphics.shader;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL30.glUniform1ui;
import static org.lwjgl.opengl.GL30.glUniform2ui;
import static org.lwjgl.opengl.GL30.glUniform3ui;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import com.alkrist.maribel.client.scenegraph.Renderable;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.graphics.filter.PPEProperty;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.ui.fonts.UIText;

public abstract class ShaderProgram {

	private int programID; // the ID for the shader program
	private Map<String, Integer> uniforms;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram() {
		programID = GL20.glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if (programID == 0){
			//TODO: logging
			System.err.println("Shader creation failed");
			System.exit(1);
		}	
	}
	
	public void addVertexShader(String text){
		addProgram(text, GL20.GL_VERTEX_SHADER);
	}
	
	public void addGeometryShader(String text){
		addProgram(text, GL32.GL_GEOMETRY_SHADER);
	}
	
	public void addFragmentShader(String text){
		addProgram(text,GL20. GL_FRAGMENT_SHADER);
	}
	
	public void addTessellationControlShader(String text){
		addProgram(text, GL40.GL_TESS_CONTROL_SHADER);
	}
	
	public void addTessellationEvaluationShader(String text){
		addProgram(text, GL40.GL_TESS_EVALUATION_SHADER);
	}
	
	public void addComputeShader(String text){
		addProgram(text, GL43.GL_COMPUTE_SHADER);
	}
	
	private void addProgram(String text, int type){
		int shader = GL20.glCreateShader(type);
		
		if (shader == 0)
		{
			System.err.println(this.getClass().getName() + " Shader creation failed");
			//TODO: logging
			System.exit(1);
		}	
		
		GL20.glShaderSource(shader, text);
		GL20.glCompileShader(shader);
		
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0)
		{
			System.err.println(this.getClass().getName() + " " + GL20.glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
		
		GL20.glAttachShader(programID, shader);
	}
	
	public void compileShader(){
		GL20.glLinkProgram(programID);

		if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
		{
			System.out.println(this.getClass().getName() + " " + GL20.glGetProgramInfoLog(programID, 1024));
			System.exit(1);
		}
		
		GL20.glValidateProgram(programID);
		
		if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0)
		{
			System.err.println(this.getClass().getName() +  " " + GL20.glGetProgramInfoLog(programID, 1024));
			System.exit(1);
		}
	}
	
	public void addUniform(String uniform) {
		int uniformLocation = GL20.glGetUniformLocation(programID, uniform);
		
		if (uniformLocation == 0xFFFFFFFF)
		{
			System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void addUniformBlock(String uniform) {
		int uniformLocation =  GL31.glGetUniformBlockIndex(programID, uniform);		
		if (uniformLocation == 0xFFFFFFFF)
		{
			System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformLocation);
	}
	
	public void setUniform(String uniformName, int value){
		glUniform1i(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, float value){
		glUniform1f(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, Vector2f value){
		glUniform2f(uniforms.get(uniformName), value.x, value.y);
	}
	
	public void setUniform(String uniformName, Vector3f value){
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}
	
	public void setUniform(String uniformName, Vector4f value){
		glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String uniformName, Matrix4f value){
		value.get(matrixBuffer);
		//matrixBuffer.flip();
		glUniformMatrix4fv(uniforms.get(uniformName), false, matrixBuffer);
	}
	
	public void setUniform(String uniformName, int x, int y) {
		glUniform2ui(uniforms.get(uniformName), x, y);
	}
	
	public void setUniform(String uniformName, int x, int y,int z) {
		glUniform3ui(uniforms.get(uniformName), x, y, z);
	}
	
	public void setUniformUnsignedInt(String uniformName, int value) {
		glUniform1ui(uniforms.get(uniformName), value);
	}
	
	public void setUniform(String uniformName, Matrix3f value){
		value.get(matrixBuffer);
		//matrixBuffer.flip();
		glUniformMatrix3fv(uniforms.get(uniformName), false, matrixBuffer);
	}
	
	public void bindUniformBlock(String uniformBlockName, int uniformBlockBinding ){
		glUniformBlockBinding(programID, uniforms.get(uniformBlockName), uniformBlockBinding);
	}
	
	public void bindFragDataLocation(String name, int index){
		glBindFragDataLocation(programID, index, name);
	}
	
	public void bind(){
		GL20.glUseProgram(programID);
	}
	
	public void updateUniforms(Entity entity) {}

	public void updateUniforms(Texture texture) {}
	
	public void updateUniforms(PPEProperty property) {}
	
	public void updateUniforms(UIText textElement) {}
	
	public void updateUniforms(Texture[] textures) {}
	
	public void updateUniforms(Renderable object){};
	
	public void updateUniforms() {}
	//TODO: add more update uniforms, depending on purpose
	
	
	protected static String readShaderFromFile(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();

		// Loads a file
		try {

			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();

		} catch (IOException e) {
			System.err.println("Could not read the file!"); // TODO: logging
			e.printStackTrace();
			System.exit(-1);
		}
		
		return shaderSource.toString();
	}
	
	/*protected static String readShaderFromFile(String fileName, String libPath) {
		
		//load shader source
		String shaderResource = readShaderFromFile(fileName);
		
		//load library source
		StringBuilder libSource = new StringBuilder();
		try {

			BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getResourceLocation(libPath)));
			String line;
			while ((line = reader.readLine()) != null) {
				libSource.append(line).append("\n");
			}
			reader.close();

		} catch (IOException e) {
			System.err.println("Could not read the file!"); // TODO: logging
			e.printStackTrace();
			System.exit(-1);
		}
		
		String libResource = libSource.toString();
		Path path = Paths.get(libPath);
		String libName = path.getFileName().toString();
		String libHeader = "#"+libName;
		
		return shaderResource.replaceFirst(libHeader, libResource);
	}*/
	
	protected static String readShaderFromFile(String fileName, String ... libPaths ) {
		
		//load shader source
		String shaderResource = readShaderFromFile(fileName);
		
		// load libs
		for(String libPath: libPaths) {
			String libResource = readLibFile(libPath);
			Path path = Paths.get(libPath);
			String libName = path.getFileName().toString();
			
			String libHeader = "#"+libName;

			shaderResource = shaderResource.replaceFirst(libHeader, libResource);
		}
		
		return shaderResource;
	}
	
	private static String readLibFile(String libPath) {
		//load library source
		StringBuilder libSource = new StringBuilder();
		try {

			BufferedReader reader = new BufferedReader(new FileReader(libPath));
			String line;
			while ((line = reader.readLine()) != null) {
				libSource.append(line).append("\n");
			}
			reader.close();

		} catch (IOException e) {
			System.err.println("Could not read the file!"); // TODO: logging
			e.printStackTrace();
			System.exit(-1);
		}
		return libSource.toString();
	}
}
