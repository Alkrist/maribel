package com.alkrist.maribel.graphics.shadow;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.alkrist.maribel.graphics.components.light.DirectionalLight;
import com.alkrist.maribel.graphics.context.GLContext;

public class PSSMCamera {

	public static final int PSSM_SPLITS = 5;
	private static final float[] SPLIT_DISTANCES = {
			0.02f,
			0.04f,
			0.1f,
			0.5f,
			1f
	};
	
	private static Matrix4f[] projectionViewMatrices;
	
	
	public static void init() {
		projectionViewMatrices = new Matrix4f[PSSM_SPLITS];
	}
	
	public static void update(DirectionalLight sourceLight) {
		Matrix4f viewMatrix = GLContext.getMainCamera().getViewMatrix();
		Matrix4f projectionMatrix = GLContext.getMainCamera().getProjectionMatrix();
		
		Vector4f lightPos = new Vector4f(sourceLight.getPosition(), 0);
        
        //Calculate orthographic projection matrix for each split
        float lastSplitDist = 0.0f;
		for(int i = 0; i < PSSM_SPLITS; i++) {
			
			float splitDist = SPLIT_DISTANCES[i];
			
			Vector3f[] frustumCorners = new Vector3f[]{
                    new Vector3f(-1.0f, 1.0f, -1.0f),
                    new Vector3f(1.0f, 1.0f, -1.0f),
                    new Vector3f(1.0f, -1.0f, -1.0f),
                    new Vector3f(-1.0f, -1.0f, -1.0f),
                    new Vector3f(-1.0f, 1.0f, 1.0f),
                    new Vector3f(1.0f, 1.0f, 1.0f),
                    new Vector3f(1.0f, -1.0f, 1.0f),
                    new Vector3f(-1.0f, -1.0f, 1.0f),
            };
			
			// Project frustum corners into world space
            Matrix4f invCam = (new Matrix4f(projectionMatrix).mul(viewMatrix)).invert();
            for (int j = 0; j < 8; j++) {
                Vector4f invCorner = new Vector4f(frustumCorners[j], 1.0f).mul(invCam);
                frustumCorners[j] = new Vector3f(invCorner.x / invCorner.w, invCorner.y / invCorner.w, invCorner.z / invCorner.w);
            }
            
            for (int j = 0; j < 4; j++) {
                Vector3f dist = new Vector3f(frustumCorners[j + 4]).sub(frustumCorners[j]);
                frustumCorners[j + 4] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(splitDist));
                frustumCorners[j] = new Vector3f(frustumCorners[j]).add(new Vector3f(dist).mul(lastSplitDist));
            }
            
            // Get frustum center
            Vector3f frustumCenter = new Vector3f(0.0f);
            for (int j = 0; j < 8; j++) {
                frustumCenter.add(frustumCorners[j]);
            }
            frustumCenter.div(8.0f);
            
            float radius = 0.0f;
            for (int j = 0; j < 8; j++) {
                float distance = (new Vector3f(frustumCorners[j]).sub(frustumCenter)).length();
                radius = java.lang.Math.max(radius, distance);
            }
            radius = (float) java.lang.Math.ceil(radius * 16.0f) / 16.0f;
            
            Vector3f maxExtents = new Vector3f(radius);
            Vector3f minExtents = new Vector3f(maxExtents).mul(-1);
            
            Vector3f lightDir = (new Vector3f(lightPos.x, lightPos.y, lightPos.z).mul(-1)).normalize();
            Vector3f eye = new Vector3f(frustumCenter).sub(new Vector3f(lightDir).mul(-minExtents.z));
            Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
            
            Matrix4f lightViewMatrix = new Matrix4f().lookAt(eye, frustumCenter, up);
            Matrix4f lightOrthoMatrix = new Matrix4f().ortho(minExtents.x, maxExtents.x, 
            		minExtents.y, maxExtents.y, 0.0f, maxExtents.z - minExtents.z, true);
            
            //Store matrix values in array
            projectionViewMatrices[i] = lightOrthoMatrix.mul(lightViewMatrix);
            
            lastSplitDist = SPLIT_DISTANCES[i];
		}
	}
	
	public static Matrix4f[] getProjectionViewMatrices() {
		return projectionViewMatrices;
	}
	
	public static float[] getSplitDistances() {
		return SPLIT_DISTANCES;
	}
}