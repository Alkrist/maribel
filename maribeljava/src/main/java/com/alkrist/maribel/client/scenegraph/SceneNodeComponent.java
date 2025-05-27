package com.alkrist.maribel.client.scenegraph;

import com.alkrist.maribel.client.math.Transform;
import com.alkrist.maribel.common.ecs.Component;

public class SceneNodeComponent implements Component {

	private Renderable renderableNode;
	
	public SceneNodeComponent(Renderable node) {
        this.renderableNode = node;
    }
	
	public Renderable getRenderable() {
        return renderableNode;
    }
	
	public void syncTransform(Transform entityTransform) {
        //renderableNode.getWorldTransform().copyFrom(entityTransform);
    }
}
