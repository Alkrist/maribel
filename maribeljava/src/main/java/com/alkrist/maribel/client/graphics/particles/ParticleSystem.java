package com.alkrist.maribel.client.graphics.particles;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.alkrist.maribel.client.graphics.Camera;
import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.common.ecs.ComponentMapper;
import com.alkrist.maribel.common.ecs.Entity;
import com.alkrist.maribel.common.ecs.Family;
import com.alkrist.maribel.common.ecs.SystemBase;
import com.alkrist.maribel.utils.ImmutableArrayList;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * Particle System. This system is in charge of: updating particles,
 * spawning/deleting particles.
 * 
 * @author Alkrist
 *
 */
public class ParticleSystem extends SystemBase {

	private ComponentMapper<ParticleEffect> particleEffectMapper;
	private ImmutableArrayList<Entity> entities;

	/**
	 * Particle System constructor. Takes no arguments.
	 */
	public ParticleSystem() {
		super();
		particleEffectMapper = ComponentMapper.getFor(ParticleEffect.class);
	}

	@Override
	public void addedToEngine() {
		entities = engine.getEntitiesOf(Family.all(ParticleEffect.class).get());
	}

	@Override
	public void update(double deltaTime) {

		// Update existing particles first
		Iterator<Entry<Texture, List<Particle>>> mapIterator = Particle.getAllParticles().entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();

			// Loop through every particle, update it, check if it's alive or not, if not,
			// remove
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				boolean alive = p.update(deltaTime);
				if (!alive) {
					iterator.remove();
					if (list.isEmpty())
						mapIterator.remove();
				}
			}
		}

		// Generate new particles
		for (Entity entity : entities) {
			generateParticles(particleEffectMapper.getComponent(entity), deltaTime);
		}
	}

	/**
	 * Generates particles based on the {@link ParticleEffect}.
	 * 
	 * @param effect    - particle effect
	 * @param deltaTime
	 */
	public void generateParticles(ParticleEffect effect, double deltaTime) {
		double particlesToSpawn = effect.pps * deltaTime;
		int count = (int) Math.floor(particlesToSpawn);
		float partialParticle = (float) (particlesToSpawn % 1);

		for (int i = 0; i < count; i++) {
			emitParticle(effect, effect.position);
		}

		if (Math.random() < partialParticle) {
			emitParticle(effect, effect.position);
		}
	}

	private void emitParticle(ParticleEffect effect, Vector3f center) {
		float dirX = (float) (Math.random() * 2f - 1f);
		float dirZ = (float) (Math.random() * 2f - 1f);
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(effect.speed);
		new Particle(effect.texture, new Vector3f(center.x, center.y, center.z), velocity, effect.gravityComplient,
				effect.lifeLength, 0, effect.scale);
	}

}
