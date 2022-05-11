package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class TestSystemI extends SystemBase{

	@SuppressWarnings("rawtypes")
	private static final ComponentMapper COMPONENT_I_MAPPER = ComponentMapper.getFor(TestComponentI.class);
	ImmutableArrayList<Entity> entities;
	
	@Override
	public void addedToEngine() {
		System.out.println("System 1 added");
		super.enable();
	}
	
	@Override
	public void update(double deltaTime) {
		entities = engine.getEntitiesOf(Family.all(TestComponentI.class).get());
		for(Entity entity: entities) {
			TestComponentI comp = (TestComponentI) COMPONENT_I_MAPPER.getComponent(entity);
			if(comp != null) {
				comp.x++;
				comp.y++;
				comp.z++;
			}
		}
		System.out.println("System 1 updated.");
	}

}
