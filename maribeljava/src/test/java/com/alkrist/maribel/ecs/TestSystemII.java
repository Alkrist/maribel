package com.alkrist.maribel.ecs;

import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.utils.ImmutableArrayList;

public class TestSystemII extends SystemBase{

	@SuppressWarnings("rawtypes")
	private static final ComponentMapper COMPONENT_II_MAPPER = ComponentMapper.getFor(TestComponentII.class);
	ImmutableArrayList<Entity> entities;
	
	@Override
	public void addedToEngine() {
		System.out.println("System 2 added");
		super.enable();
	}
	
	@Override
	public void update(double deltaTime) {
		entities = engine.getEntitiesOf(Family.all(TestComponentII.class).get());
		for(Entity entity: entities) {
			TestComponentII comp = (TestComponentII) COMPONENT_II_MAPPER.getComponent(entity);
			if(comp!=null) {
				comp.floatNumber+=1.0f;
			}
		}
		System.out.println("System 2 updated.");
	}

}
