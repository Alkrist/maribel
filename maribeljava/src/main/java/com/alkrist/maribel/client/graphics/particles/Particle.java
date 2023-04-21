package com.alkrist.maribel.client.graphics.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alkrist.maribel.graphics.context.Camera;
import com.alkrist.maribel.graphics.texture.Texture2D;

/**
 * This class represents a single particle. Also it keeps a list of all
 * particles that are currently on the render engine.
 * 
 * @author Alkrist
 *
 */
public class Particle {

	/*private static Map<Texture2D, List<Particle>> allParticles = new HashMap<Texture2D, List<Particle>>();

	public Vector3f position;
	public Vector3f velocity;

	public float gravityEffect;
	public float lifeLength;
	public float rotation;
	public float scale;

	// This should be texture atlas in order to make particle change over time
	private Texture2D texture;

	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	private float elapsedTime = 0;
	// private float distance;

	/**
	 * Particle constructor. Called from {@link ParticleSystem}. When called, adds
	 * this new particle to the list of all particles.
	 * 
	 * @param texture       - particle texture
	 * @param position      - particle world position
	 * @param velocity      - particle current velocity
	 * @param gravityEffect - gravity effect of this particle
	 * @param lifeLength    - how long can this particle live
	 * @param rotation      - particle rotation
	 * @param scale         - particle scale
	 */
	/*protected Particle(Texture2D texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength,
			float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		registerParticle();
	}*/

	/**
	 * Updates this particle's transform and state.
	 * 
	 * @param deltaTime
	 * @return if this particle should be deleted
	 */
	/*protected boolean update(double deltaTime) {
		// TODO: replace -80 by the gravity variable later
		velocity.y += -80 * gravityEffect * deltaTime;
		Vector3f change = new Vector3f(velocity.x, velocity.y, velocity.z);
		change.scale((float) deltaTime);
		Vector3f.add(change, position, position);
		// distance = Vector3f.sub(camera.position, position, null).lengthSquared();
		updateTextureCoordsData();
		elapsedTime += deltaTime;
		return elapsedTime < lifeLength;
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) column / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

	private void updateTextureCoordsData() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;

		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;

		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2); // TODO: ????? - is this right?
	}
*/
	/*protected float getDistance(Camera camera) {
		return Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
	}

	private void registerParticle() {
		List<Particle> list = allParticles.get(texture);
		if (list == null) {
			list = new ArrayList<Particle>();
			allParticles.put(texture, list);
		}
		list.add(this);
	}*/

	/*protected float getBlend() {
		return blend;
	}

	protected Vector2f getTextureOffset1() {
		return texOffset1;
	}

	protected Vector2f getTextureOffset2() {
		return texOffset2;
	}

	/**
	 * 
	 * @return list of all particles on the render engine.
	 */
	/*public static Map<Texture2D, List<Particle>> getAllParticles() {
		return allParticles;
	}*/
}
