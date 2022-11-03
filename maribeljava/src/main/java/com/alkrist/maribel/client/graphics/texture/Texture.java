package com.alkrist.maribel.client.graphics.texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.alkrist.maribel.client.Settings;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

/**
 * This class holds information about a texture and
 * methods to operate with it.
 * 
 * Inspired by Heiko Brumme's Texture implementation.
 * 
 * @author Mikhail
 *
 */
public class Texture {
	
	private int id;
	private int width;
	private int height;
	
	//TODO: new stuff
	private int numberOfRows = 1;
	
	private Texture() {
		this.id = GL11.glGenTextures();
	}
	
	/**
     * Sets a parameter of the texture.
     *
     * @param name  Name of the parameter
     * @param value Value to set
     */
    public void setParameter(int name, int value) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, name, value);
    }
    
    /**
     * Uploads image data with specified width and height.
     *
     * @param width  Width of the image
     * @param height Height of the image
     * @param data   Pixel data of the image
     */
    public void uploadData(int width, int height, ByteBuffer data) {
        uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);
    }
    
    /**
     * Uploads image data with specified internal format, width, height and
     * image format.
     *
     * @param internalFormat Internal format of the image data
     * @param width          Width of the image
     * @param height         Height of the image
     * @param format         Format of the image data
     * @param data           Pixel data of the image
     */
    public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL11.GL_UNSIGNED_BYTE, data);
    }
    
    /**
     * Binds the texture.
     */
    public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
    
    /**
     * Delete the texture.
     */
    public void delete() {
        GL11.glDeleteTextures(id);
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
	public int getTextureId() {
		return id;
	}
	
	//TODO: new stuff
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	//TODO: new stuff
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        }
    }
    
    public void setWidth(int width) {
        if (width > 0) {
            this.width = width;
        }
    }
    
    private static Map<String, Texture> allTextures = new HashMap<String, Texture>();
    
    /**
     * Creates a texture with specified width, height and data.
     *
     * @param width  Width of the texture
     * @param height Height of the texture
     * @param data   Picture Data in RGBA format
     *
     * @return Texture from the specified data
     */
    public static Texture createTexture(int width, int height, ByteBuffer data) {
        Texture texture = new Texture();
        texture.setWidth(width);
        texture.setHeight(height);

        texture.bind();

        /*if(Settings.CURRENT.mipmapEnabled) {
    		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    		texture.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
    		texture.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
    		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
    	}*/
        
        texture.setParameter(GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        texture.setParameter(GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        texture.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        texture.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        
        texture.uploadData(GL11.GL_RGBA8, width, height, GL11.GL_RGBA, data);

        return texture;
    }
    
    /**
     * Load texture from file.
     *
     * @param path - File path of the texture
     *
     * @return Texture from specified file
     */
    public static Texture loadTexture(String path) {
    	if(allTextures.get(path)!=null) {
    		return allTextures.get(path);
    	}else {
    		ByteBuffer image;
	        int width, height;
	        
	        try (MemoryStack stack = MemoryStack.stackPush()) {
	            /* Prepare image buffers */
	            IntBuffer w = stack.mallocInt(1);
	            IntBuffer h = stack.mallocInt(1);
	            IntBuffer comp = stack.mallocInt(1);
	
	            /* Load image */
	            //STBImage.stbi_set_flip_vertically_on_load(true);
	            image = STBImage.stbi_load(FileUtil.getTexturesPath()+path+".png", w, h, comp, 4);
	            if (image == null) {
	            	Logging.getLogger().log(Level.WARNING, "Failed to load texture file");
	          	  	return null;
	            }
	
	            /* Get width and height of image */
	            width = w.get();
	            height = h.get();
	        }
	        
	        Texture tex = createTexture(width, height, image);
	        allTextures.put(path, tex);
	        return tex;
    	}
    }
    
    /**
     * TODO: new stuff.
     * Load texture atlas from file.
     * 
     * @param path - File path of the texture
     * @param numberOfRows - number of rows of the texture atlas
     * 
     * @return Texture object of the texture atlas
     */
    public static Texture loadTexture(String path, int numberOfRows) {
    	Texture texture = loadTexture(path);
    	texture.setNumberOfRows(numberOfRows);
    	return texture;
    }
    
}
