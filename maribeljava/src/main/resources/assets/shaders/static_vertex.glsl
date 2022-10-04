#version 400 core

in vec3 position;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 surfaceNormal;
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
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_texCoords = (texCoords / numberOfRows) + textureOffset;
	//pass_texCoords = texCoords;
	
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	for(int i=0; i<4; i++){
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}
