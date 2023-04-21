#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec2 textureCoords_FS;

void main(void){

	mat4 viewModelMatrix = viewMatrix * modelMatrix;
	vec4 positionRelativeToCamera = viewModelMatrix * vec4(position,1.0);
	vec4 worldPos = projectionMatrix * positionRelativeToCamera;
	gl_Position = worldPos;

	/*gl_ClipDistance[0] = dot(gl_Position,frustumPlanes[0]);
	gl_ClipDistance[1] = dot(gl_Position,frustumPlanes[1]);
	gl_ClipDistance[2] = dot(gl_Position,frustumPlanes[2]);
	gl_ClipDistance[3] = dot(gl_Position,frustumPlanes[3]);
	gl_ClipDistance[4] = dot(gl_Position,frustumPlanes[4]);
	gl_ClipDistance[5] = dot(gl_Position,frustumPlanes[5]);*/

	textureCoords_FS = textureCoords;

}
