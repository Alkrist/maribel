#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

uniform mat4 projectionMatrix;
uniform mat4 viewModelMatrix;
//uniform vec4 frustumPlanes[6];

out vec3 normal_FS;
out vec3 tangent_FS;
out vec3 bitangent_FS;
out vec2 textureCoords_FS;
out vec3 position_FS;

void main(void){

	vec4 positionRelativeToCamera = viewModelMatrix * vec4(position,1.0);
	vec4 worldPos = projectionMatrix * positionRelativeToCamera;
	gl_Position = worldPos;


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


	//OTHERS
	normal_FS = norm;
	tangent_FS = tang;
	textureCoords_FS = textureCoords;
	position_FS = worldPos.xyz;
}

