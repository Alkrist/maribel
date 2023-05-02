#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
//uniform vec4 frustumPlanes[6];

out vec3 normal_FS;
out vec3 tangent_FS;
out vec3 bitangent_FS;
out vec2 textureCoords_FS;
out vec3 position_FS;

void main(void){

	mat4 viewModelMatrix = viewMatrix * modelMatrix;
	vec4 outWorldPosition = modelMatrix * vec4(position, 1.0);
	vec4 outViewPosition  = viewMatrix * outWorldPosition;

	gl_Position   = projectionMatrix * outViewPosition;


	//FRUSTUM PLANES
	/*gl_ClipDistance[0] = dot(gl_Position,frustumPlanes[0]);
	gl_ClipDistance[1] = dot(gl_Position,frustumPlanes[1]);
	gl_ClipDistance[2] = dot(gl_Position,frustumPlanes[2]);
	gl_ClipDistance[3] = dot(gl_Position,frustumPlanes[3]);
	gl_ClipDistance[4] = dot(gl_Position,frustumPlanes[4]);
	gl_ClipDistance[5] = dot(gl_Position,frustumPlanes[5]);*/


	//BITANGENT
	vec3 surfaceNormal = (projectionMatrix * viewModelMatrix * vec4(normal,0.0)).xyz;
	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((viewModelMatrix * vec4(tangent, 0.0)).xyz);

	bitangent_FS = normalize(cross(norm, tang));

	//TODO: fix normal maps according to new matrix calculations,fix position in deferred lighting
	//OTHERS
	normal_FS = norm;
	tangent_FS = tang;
	textureCoords_FS = textureCoords;
	position_FS = outWorldPosition.xyz;
}

