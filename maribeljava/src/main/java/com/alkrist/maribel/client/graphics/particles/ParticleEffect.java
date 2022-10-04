package com.alkrist.maribel.client.graphics.particles;

import com.alkrist.maribel.client.graphics.texture.Texture;
import com.alkrist.maribel.common.ecs.Component;
import com.alkrist.maribel.utils.math.Vector3f;

/**
 * Represents one particle effect. Keeps data about particle effect behavior as
 * well as some starting values for single {@link Particle}.
 * 
 * @author Alkrist
 *
 */
public class ParticleEffect implements Component {

	public float pps;
	public float speed;
	public float gravityComplient;
	public float lifeLength;
	public Vector3f position;

	// TODO:think of how to get rid of texture object in sake of Server-Client
	// architecture
	public Texture texture;

	/**
	 * Particle effect constructor.
	 * 
	 * @param texture          - particle texture (atlas)
	 * @param pps              - emit particles per second
	 * @param speed            - how fast do particles emit
	 * @param gravityComplient - how much is particle affected by gravity
	 * @param lifeLength       - how long the particle can live
	 * @param position         - particle generator position
	 */
	public ParticleEffect(Texture texture, float pps, float speed, float gravityComplient, float lifeLength,
			Vector3f position) {
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
		this.texture = texture;
		this.position = position;
	}

}
