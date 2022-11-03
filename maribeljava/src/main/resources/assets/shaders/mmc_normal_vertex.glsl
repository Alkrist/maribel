#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoords;
out vec3 toLightVector[4];
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];

uniform float numberOfRows;
uniform vec2 textureOffset;

void main(void){
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = modelViewMatrix * vec4(position,1.0);
	gl_Position = projectionMatrix * positionRelativeToCamera;

	pass_textureCoords = (textureCoords/numberOfRows) + textureOffset;

	vec3 surfaceNormal = (projectionMatrix * modelViewMatrix * vec4(normal,0.0)).xyz;

	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));

	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);

	for(int i=0;i<4;i++){
		toLightVector[i] = toTangentSpace * (lightPosition[i] - positionRelativeToCamera.xyz);
	}

	toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);
}
