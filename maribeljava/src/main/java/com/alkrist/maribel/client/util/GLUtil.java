package com.alkrist.maribel.client.util;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
//import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
//import static org.lwjgl.opengl.GL13.GL_SAMPLE_ALPHA_TO_COVERAGE;
//import static org.lwjgl.opengl.GL20.GL_POINT_SPRITE;
//import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;
//import static org.lwjgl.opengl.GL32.GL_SAMPLE_MASK;

public class GLUtil {

	/*
	 * Note for later: some flags are copied from oreon-engine and are indeed not needed on init stage,
	 * uncomment them if needed later, 
	 * 
	 * YET DON'T FUCKING YOU DARE TO DELETE THEM!!!
	 */
	
	
	public static void init() {

		// culling and vertex order
		glFrontFace(GL_CW);				
		glEnable(GL_CULL_FACE);	
		glCullFace(GL_BACK);
		
		// fbo
		glEnable(GL_DEPTH_TEST);     	
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_FRAMEBUFFER_SRGB);
		
		// multisampling and other stuff
		//glEnable(GL_POINT_SPRITE);
		//glEnable(GL_MULTISAMPLE);
		//glEnable(GL_PROGRAM_POINT_SIZE);
		//glEnable(GL_SAMPLE_ALPHA_TO_COVERAGE);
		//glEnable(GL_SAMPLE_MASK);
		
		// clip planes
		/*glEnable(GL_CLIP_DISTANCE0);
		glEnable(GL_CLIP_DISTANCE1);
		glEnable(GL_CLIP_DISTANCE2);
		glEnable(GL_CLIP_DISTANCE3);
		glEnable(GL_CLIP_DISTANCE4);
		glEnable(GL_CLIP_DISTANCE5);*/
	}

	public static void clearScreen() {
		glClearColor(0f,0f,0f,1.0f);
		glClearDepth(1.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
}
