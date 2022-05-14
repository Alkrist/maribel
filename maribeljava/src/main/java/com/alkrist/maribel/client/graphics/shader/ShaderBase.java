package com.alkrist.maribel.client.graphics.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.math.Matrix2f;
import com.alkrist.maribel.utils.math.Matrix3f;
import com.alkrist.maribel.utils.math.Matrix4f;
import com.alkrist.maribel.utils.math.Vector2f;
import com.alkrist.maribel.utils.math.Vector3f;
import com.alkrist.maribel.utils.math.Vector4f;

public abstract class ShaderBase {

	private int programID; // the ID for the shader program
	private int vertexShaderID; // ID of the vertex shader compiled code
	private int fragmentShaderID; // ID for the fragment shader compiled code

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	/**
	 * Constructor for shader program. Requires vertex and fragment shaders. Takes
	 * two shader file names WITHOUT extension and WITHOUT full path, just names.
	 * 
	 * Shaders must be stored in src/main/resources/assets/shaders folder and mush
	 * have .glsl extension!
	 * 
	 * @param vertexShader   - name of the vertex shader file without extension
	 * @param fragmentShader - name of the fragment shader file without extension
	 */
	public ShaderBase(String vertexShader, String fragmentShader) {

		// load the shaders and make the program
		vertexShaderID = loadShader(FileUtil.getShadersPath() + vertexShader + ".glsl", GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(FileUtil.getShadersPath() + fragmentShader + ".glsl", GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();

		// Attach the shaders to the program and validate it
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();

	}

	// UNIFORM LOCATIONS &
	// LOADING------------------------------------------------------------------
	// Uniform location finder
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	protected abstract void getAllUniformLocations();

	// load a float value to the location
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	// load a 3d vector to the location
	protected void loadVector3f(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	// load a 4d vector to the location
	protected void loadVector4f(int location, Vector4f vector) {
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}

	// to load a 2d vector
	protected void loadVector2f(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}

	// load a boolean value as float to the location (GLSL ain't have a boolean
	// type)
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value)
			toLoad = 1;
		GL20.glUniform1f(location, toLoad);
	}

	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	protected void loadMatrix4f(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}

	protected void loadMatrix3f(int location, Matrix3f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix3fv(location, false, matrixBuffer);
	}

	protected void loadMatrix2f(int location, Matrix2f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix2fv(location, false, matrixBuffer);
	}
	// ----------------------------------------------------------------------------------

	// start the shader usage
	public void start() {
		GL20.glUseProgram(programID);
	}

	// stop it
	public void stop() {
		GL20.glUseProgram(0);
	}

	// remove the shaders created and the program created -- used in the end to
	// release memory
	public void cleanUp() {
		stop();

		// Deattach shaders from the program
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);

		// delete these shaders and the program itself
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	// bind the attributes -- will use the bindAttribute method in the inherited
	// classes
	protected abstract void bindAttributes();

	// binds the attribute from VAO
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	// Loads a shader code from file and compiles it
	private static int loadShader(String file, int type) {

		StringBuilder shaderSource = new StringBuilder();

		// Loads a file
		try {

			BufferedReader reader = new BufferedReader(new FileReader(file));
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

		// Creates a shader by type and compiles the code for that from the file opened
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		// In case of failure -- terminate
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500)); // TODO; logging
			System.err.println("Could not compile shader!"); // TODO; logging
			System.exit(-1);

		}
		return shaderID;
	}
}
