package com.alkrist.maribel.client.preload;

import java.util.HashMap;
import java.util.Map;

import com.alkrist.maribel.client.graphics.model.ModelComposite;

/**
 * Stores all model composites. Client-side class.
 * Models are mapped by their name.
 * 
 * Add new on init stage.
 * 
 * When creating a {@link Model} component, not load a new model, get one from here,
 * in sake of saving memory, hence the Model component will have only a link, not a model itself.
 * 
 * @author Alkrist
 *
 */
public class ModelsPreload {

	private static Map<String, ModelComposite> models = new HashMap<String, ModelComposite>();
	
	/**
	 * loads a new {@link ModelComposite} to the preload.
	 * 
	 * @param model - Model composite object
	 */
	public static void addModel(ModelComposite model) {
		models.put(model.getName(), model);
	}
	
	/**
	 * Returns an existing {@link ModelComposite} from this preload.
	 * 
	 * @param name - name of a model
	 * @return Model Composite or null if the model deesn't exist
	 */
	public static ModelComposite getModel(String name) {
		return models.get(name);
	}
	
	/**
	 * Cleans the model composite preload.
	 */
	public static void cleanUp() {
		models.clear();
	}
}
