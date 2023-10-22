package com.alkrist.maribel.graphics.components;

import java.util.Comparator;
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
	
	private Float weight;
	
	private PostProcessingVolume(float weight, Bits effectBits, Map<Integer, PPEProperty> props) {
		
		this.weight = weight;
		
		this.effectBits = effectBits;
		this.effectProperties = props;
	}
	
	public PPEProperty getUniformProperty(int id) {
		return effectProperties.get(id);
	}
	
	public boolean hasEffect(int id) {
		return effectBits.get(id);
	}
	
	public Float getWeight() {
		return weight;
	}
	
	public void setWeight(float weight) {
		weight = Math.max(0, weight);
		weight = Math.min(weight, 1);
		
		this.weight = weight;
	}
	
	public boolean isEnabled() {
		return weight > 0;
	}
	
	public static class PPEComponentBuilder {
		
		private float weight;
		
		private Map<Integer, PPEProperty> props;
		private Bits effectBits;
		
		public PPEComponentBuilder(float weight) {
			
			weight = Math.max(0, weight);
			weight = Math.min(weight, 1);
			this.weight = weight;
			
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
			return new PostProcessingVolume(weight, effectBits, props);
		}
	}
	
	public static class PPEVolumeComparator implements Comparator<PostProcessingVolume> {
	    @Override
	    public int compare(PostProcessingVolume o1, PostProcessingVolume o2) {
	        return o1.getWeight().compareTo(o2.getWeight());
	    }
	}
}
