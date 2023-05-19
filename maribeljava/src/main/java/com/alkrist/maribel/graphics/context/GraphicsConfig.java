package com.alkrist.maribel.graphics.context;

import java.util.Properties;

import com.alkrist.maribel.client.settings.ConfigBase;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;

public class GraphicsConfig extends ConfigBase{

	public final float NEAR_PLANE = 0.1f;
	public final float FAR_PLANE = 10000; 
	
	
	public float fovY;
	public boolean vsyncEnabled;
	public boolean fullscreen;
	public int width;
	public int height;
	public SamplerFilter samplerFilter;
	public int shadowMapResolution;
	public boolean isShadowMapsEnabled;
	public boolean isSSAOEnabled;
	public boolean isMultisamplingEnabled;
	public int multisampleSamplesCount;
	
	public GraphicsConfig() {}
	
	@Override
	protected void loadProperties(Properties prop) {
		fovY = Float.valueOf(prop.getProperty("FOV", "70"));
		vsyncEnabled = Boolean.valueOf(prop.getProperty("vsync", "false"));
		fullscreen = Boolean.valueOf(prop.getProperty("fullscreen_mode", "false"));
		String filter = prop.getProperty("samplerFilter", "trilinear");
		switch(filter) {
			case "nearest": samplerFilter = SamplerFilter.Nearest; break;
			case "bilinear": samplerFilter = SamplerFilter.Bilinear; break;
			case "trilinear": samplerFilter = SamplerFilter.Trilinear; break;
			case "anisotropic": samplerFilter = SamplerFilter.Anisotropic; break;
			default: samplerFilter = SamplerFilter.Trilinear; break; //Set mipmapping if some shit is written in config
		}
		width = Integer.valueOf(prop.getProperty("width", "1280"));
		height = Integer.valueOf(prop.getProperty("height", "720"));
		shadowMapResolution = Integer.valueOf(prop.getProperty("shadowmap_resolution", "2048"));
		isShadowMapsEnabled = Boolean.valueOf(prop.getProperty("shadowmap_enabled", "true"));
		isSSAOEnabled = Boolean.valueOf(prop.getProperty("ssao_enabled", "true"));
		isMultisamplingEnabled = Boolean.valueOf(prop.getProperty("mulitsampling_enabled", "true"));
		multisampleSamplesCount = Integer.valueOf(prop.getProperty("multisample_count", "2"));
	}

	@Override
	protected void saveProperties(Properties prop) {
		prop.setProperty("FOV", String.valueOf(fovY));
		prop.setProperty("vsync", String.valueOf(vsyncEnabled));
		prop.setProperty("fullscreen_mode", String.valueOf(fullscreen));
		switch(samplerFilter) {
			case Nearest: prop.setProperty("samplerFilter", "nearest"); break;
			case Bilinear: prop.setProperty("samplerFilter", "bilinear"); break;
			case Trilinear: prop.setProperty("samplerFilter", "trilinear"); break;
			case Anisotropic: prop.setProperty("samplerFilter", "anisotropic"); break;
		}
		prop.setProperty("width", String.valueOf(width));
		prop.setProperty("height", String.valueOf(height));
		prop.setProperty("shadowmap_resolution", String.valueOf(shadowMapResolution));
		prop.setProperty("shadowmap_enabled", String.valueOf(isShadowMapsEnabled));
		prop.setProperty("ssao_enabled", String.valueOf(isSSAOEnabled));
		prop.setProperty("mulitsampling_enabled", String.valueOf(isMultisamplingEnabled));
		prop.setProperty("multisample_count", String.valueOf(multisampleSamplesCount));
	}

}
