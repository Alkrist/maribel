package com.alkrist.maribel.graphics.components;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.graphics.filter.PPEProperty;
import com.alkrist.maribel.graphics.filter.contrast.ContrastController;
import com.alkrist.maribel.graphics.filter.contrast.ContrastProperty;
import com.alkrist.maribel.utils.Bits;

public class PostProcessingVolume implements Component{

	private Bits effectBits;
	private Map<Integer,PPEProperty> effectProperties;
	
	private PostProcessingVolume(Bits effectBits, Map<Integer, PPEProperty> props) {
		
		this.effectBits = effectBits;
		this.effectProperties = props;
	}
	
	public PPEProperty getUniformProperty(int id) {
		return effectProperties.get(id);
	}
	
	public boolean hasEffect(int id) {
		return effectBits.get(id);
	}
	
	public static class PPEComponentBuilder {
		
		private Map<Integer, PPEProperty> props;
		private Bits effectBits;
		
		public PPEComponentBuilder() {
			props = new HashMap<Integer, PPEProperty>();
			effectBits = new Bits();
		}
		
		public PPEComponentBuilder addEffectContrast(ContrastProperty prop) {
			effectBits.set(ContrastController.ID);
			props.put(ContrastController.ID, prop);
			return this;
		}
		
		//Add more effects here
		
		public PostProcessingVolume get() {
			return new PostProcessingVolume(effectBits, props);
		}
	}
}
