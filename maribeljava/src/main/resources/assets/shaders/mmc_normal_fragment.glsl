#version 400 core

in vec2 pass_textureCoords;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in vec3 surfaceNormal; //in case normal map isn't used
in float pass_hasNormalMap;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform sampler2D normalMap;

uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;

void main(void){

	// COMMON
	vec3 unitToCameraVector = normalize(toCameraVector);


	//UNIT NORMAL VECTOR CALCULATION
	vec3 unitNormal = vec3(0.0);

	//WITH NORMAL MAP
	if(pass_hasNormalMap == 1.0){
		vec4 normalMapValue = 2.0 * texture(normalMap, pass_textureCoords) - 1.0;
		unitNormal = normalize(normalMapValue.rgb);
	}

	//WITHOUT NORMAL MAP
	else{
		unitNormal = normalize(surfaceNormal);
	}


	//PER-PIXEL LIGHT CALCULATION
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for(int i=0; i<4; i++){

			float distance = length(toLightVector[i]);
			float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

			vec3 unitLightVector = normalize(toLightVector[i]);
			float nDotl = dot(unitNormal, unitLightVector);
			float brightness = max(nDotl, 0.0);
			vec3 lightDirection = -unitLightVector;
			vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
			float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
			specularFactor = max(specularFactor, 0.0);
			float dampedFactor = pow(specularFactor, shineDamper);
			totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
			totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
		}

	totalDiffuse = max(totalDiffuse, 0.2);

	vec4 textureColor = texture(modelTexture, pass_textureCoords);
	if(textureColor.a < 0.3){
		discard;
	}

	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
}
