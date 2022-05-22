#version 400 core

in vec2 pass_texCoords;

out vec4 out_Color;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void){

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitToCameraVector = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	for(int i=0; i<4; i++){
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + brightness * lightColor[i];
		totalSpecular = totalSpecular + dampedFactor * reflectivity * lightColor[i];
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColor = texture(textureSampler, pass_texCoords);
	
	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
}