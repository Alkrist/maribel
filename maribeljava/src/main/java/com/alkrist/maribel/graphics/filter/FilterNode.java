package com.alkrist.maribel.graphics.filter;

import com.alkrist.maribel.graphics.context.GLContext;
import com.alkrist.maribel.graphics.texture.Texture;
import com.alkrist.maribel.graphics.texture.Texture.ImageFormat;
import com.alkrist.maribel.graphics.texture.Texture.SamplerFilter;
import com.alkrist.maribel.graphics.texture.Texture.TextureWrapMode;
import com.alkrist.maribel.graphics.texture.Texture2D;

//TODO: check usability
public abstract class FilterNode {

	private FilterNode parent;
	private FilterNode child;
	private String name;
	private boolean enabled = false;
	
	private Texture outputTexture;
	
	public FilterNode(String name) {
		outputTexture = new Texture2D(GLContext.getConfig().width,
				GLContext.getConfig().height,
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		this.name = name;
		parent = null;
		child = null;
	}
	
	public final Texture getOutputTexture() {
		if(parent != null) {
			return enabled == true ? outputTexture : parent.getOutputTexture();
		}else {
			return enabled == true ? outputTexture : null;
		}
		
	}
	
	public final FilterNode getParent() {
		return parent;
	}
	
	public final FilterNode getChild() {
		return child;
	}
	
	protected final void setParent(FilterNode parent) {
		this.parent = parent;
	}
	
	protected final void setChild(FilterNode child) {
		this.child = child;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setEnabled(boolean state) {
		this.enabled = state;
	}
	
	public final boolean isEnabled() {
		return enabled;
	}
	
	public final void render(Texture scene, Texture lightScatteringMask, Texture specularEmissionSsaoBlurMask,
			Texture opaqueDepth, Texture transparencyDepth) {
		
		if(enabled) {
			Texture source = parent == null ? scene : parent.getOutputTexture();
			if(source == null) source = scene;
			
			compute(source, outputTexture, lightScatteringMask, specularEmissionSsaoBlurMask, opaqueDepth, transparencyDepth);
					
		}
		
		if(child == null) {
			scene = enabled == true ? outputTexture : scene;
		}else {
			child.render(scene, lightScatteringMask, specularEmissionSsaoBlurMask, opaqueDepth, transparencyDepth);
		}
	}
	
	protected abstract void compute(Texture source, Texture dest, Texture lightScattering, Texture specularEmissionSsaoBlur,
			Texture opaqueDepth, Texture transparencyDepth);
	
	public final void addChild(FilterNode node, String parentName) {
		if(this.child != null) {
			if(this.name.equals(parentName)) {
				node.setParent(this);
				node.setChild(this.child);
				this.child.setParent(node);
				this.setChild(node);
			}else {
				this.child.addChild(node, parentName);
			}
		}else {
			node.setParent(this);
			setChild(node);
		}
	}
	
	@Override
	public final String toString() {
		if(child != null)
			return name + " -> " + child.toString();
		else
			return name;
	}
}
