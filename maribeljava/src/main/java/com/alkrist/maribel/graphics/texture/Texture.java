package com.alkrist.maribel.graphics.texture;


import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_MIRRORED_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.opengl.GL42.glTexStorage3D;

import java.nio.ByteBuffer;
import java.util.logging.Level;

import org.lwjgl.opengl.GL;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.utils.FileUtil;
import com.alkrist.maribel.utils.Logging;

public class Texture {

	private int target;
	
	private int id;
	
	private ImageMeta meta;
	
	private int numberOfRows = 1;
	
	
	public Texture(int target, int width, int height) {
		this.id = glGenTextures();
		this.target = target;
		this.meta = new ImageMeta(width, height);
	}
	
	public Texture(String path) {
		this.id = glGenTextures();
		this.target = GL_TEXTURE_2D;
		this.meta = ImageLoader.loadImage(FileUtil.getTexturesPath()+path, id);
	}
	
	public void bind() {
		glBindTexture(target, id);
	}
	
	public void unbind() {
		glBindTexture(target, 0);
	}
	
	public void delete() {
		glDeleteTextures(id);
	}
	
	public ImageMeta getMetaData() {
		return meta;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public void activeTextureUnit(int unit){
		glActiveTexture(GL_TEXTURE0 + unit);
	}

	public void nearestFilter() {
    	glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    	glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }
    
    public void bilinearFilter() {
    	glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }
    
    public void trilinearFilter() {
    	glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    	glGenerateMipmap(target);
    	glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    	glTexParameterf(target, GL_TEXTURE_LOD_BIAS, -1);
    }
    
    public void anisotropicFilter() {
    	if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
    		glGenerateMipmap(target);
        	glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        	glTexParameterf(target, GL_TEXTURE_LOD_BIAS, 0);
        	
    		float maxfilterLevel = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
    		glTexParameterf(target, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxfilterLevel);
    	}else {
    		Logging.getLogger().log(Level.WARNING, "Anisotropic filtering is not supported!");
    		trilinearFilter(); //TODO: can get rid of it completely, it's deprecated
    		GLContext.getConfig().samplerFilter = SamplerFilter.Trilinear;
    	}
    }
    
    public void clampToEdge() {
    	glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    	glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }
    
    public void clampToBorder() {
    	glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
    	glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
    }
    
    public void repeat() {
    	glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
    	glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }
    
    public void mirroredRepeat() {
    	glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
    	glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
    }
    
    public void allocateImage2D(int internalFormat, int format, int type, ByteBuffer data){
		glTexImage2D(target, 0, internalFormat, meta.getWidth(), meta.getHeight(), 0, format, type, data);
	}
	
	public void allocateImage2D(int internalFormat, int format, int type){
		glTexImage2D(target, 0, internalFormat, meta.getWidth(), meta.getHeight(), 0, format, type, (ByteBuffer) null);
	}
	
	public void allocateImage2DMultisample(int samples, int internalFormat){
		glTexImage2DMultisample(target, samples, internalFormat, meta.getWidth(), meta.getHeight(), true);
	}
	
	public void allocateStorage2D(int levels, int internalFormat){
		glTexStorage2D(target, levels, internalFormat, meta.getWidth(), meta.getHeight());
	}
	
	public void allocateStorage3D(int levels, int layers, int internalFormat){
		glTexStorage3D(target,levels,internalFormat,meta.getWidth(),meta.getHeight(),layers);
	}
	
	public enum ImageFormat {
		RGBA8_SNORM,
		RGBA32FLOAT,
		RGB32FLOAT,
		RGBA16FLOAT,
		DEPTH32FLOAT,
		R16FLOAT,
		R32FLOAT,
		R8
	}
	
	public enum SamplerFilter {

		Nearest,
		Bilinear,
		Trilinear,
		Anisotropic
	}
	
	public enum TextureWrapMode {
		
		ClampToEdge,
		ClampToBorder,
		Repeat,
		MirrorRepeat
	}
}
