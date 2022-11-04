#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoords;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out vec3 surfaceNormal; //in case normal map isn't used
out float pass_hasNormalMap;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];

uniform float numberOfRows;
uniform vec2 textureOffset;

uniform float hasNormalMap;

void main(void){

	// COMMON - POSITION, TEXTURE COORDINATES
	vec4 worldPosition = transformationMatrix * vec4(position,1.0);
	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = modelViewMatrix * vec4(position,1.0);
	gl_Position = projectionMatrix * positionRelativeToCamera;
	pass_textureCoords = (textureCoords/numberOfRows) + textureOffset;

	vec3 surfaceNormal = vec3(0.0);
	pass_hasNormalMap = hasNormalMap;

	// LIGHT AND CAMERA VECTOR CALCULATION

	//WITH NORMAL MAP
	if (hasNormalMap == 1.0){
		surfaceNormal = (projectionMatrix * modelViewMatrix * vec4(normal,0.0)).xyz;

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

	//WITHOUT NORMAL MAP
	else{
		surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;

		for(int i=0; i<4; i++){
			toLightVector[i] = lightPosition[i] - worldPosition.xyz;
		}

		toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	}
}
