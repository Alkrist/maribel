#version 430 core

in vec2 texCoord;

uniform sampler2D albedoSampler;
uniform sampler2D worldPositionSampler;
uniform sampler2D normalSampler;
uniform sampler2D specular_emission_diffuse_ssao_bloom_Sampler;

layout(location = 0) out vec4 deferred_out;

struct PointLight
{
    vec4 position;
    vec4 color;
    float intensity;
    float radius;
};

struct Cluster
{
    vec4 minPoint;
    vec4 maxPoint;
    uint count;
    uint lightIndices[100];
};

layout(std430, binding = 1) restrict buffer clusterSSBO
{
    Cluster clusters[];
};

layout(std430, binding = 2) restrict buffer lightSSBO
{
    PointLight pointLight[];
};

uniform float zNear;
uniform float zFar;
uniform uvec3 gridSize;
uniform uvec2 screenDimensions;

//uniform sampler2D depthMap;
uniform mat4 viewMatrix;

const float constant = 1.0;
const float linear = 0.01;
const float exponent = 0.002;

vec3 calculateLightColor(vec3 color, vec3 normal, vec3 position, vec3 to_light_dir, vec3 lightColor, float light_intensity, float emission, float reflectance){

	vec3 diffuseColor = vec3(0);
	vec3 specularColor = vec3(0);

	//diffuse
	float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
	diffuseColor =  lightColor * diffuseFactor * light_intensity;
	diffuseColor = max(diffuseColor, 0.02);

	//specular
	vec3 camera_direction = normalize(-position);
	vec3 from_light_dir = -to_light_dir;
	vec3 reflected_light = normalize(reflect(from_light_dir, normal));
	float specularFactor = max(dot(camera_direction, reflected_light), 0.0);
	specularFactor = pow(specularFactor, emission);
	specularColor = light_intensity  * specularFactor * reflectance * lightColor;

	//diffuse + specular
	return (diffuseColor * color + specularColor);
}

float cubicGaussian(float h) {
      if (h < 1.0) {
        return 0.25 * pow(2.0 - h, 3.0) - pow(1.0 - h, 3.0);
      } else if (h < 2.0) {
        return 0.25 * pow(2.0 - h, 3.0);
      } else {
        return 0.0;
      }
    }

vec3 calculatePointLight(vec3 color, vec3 position, vec3 normal, PointLight pointLight, float reflectance, float emission){
	vec3 light_direction = pointLight.position.xyz - position;
	vec3 to_light_dir  = normalize(light_direction);
	vec3 lightColor = calculateLightColor(color, normal, position, to_light_dir,
			pointLight.color.xyz, pointLight.intensity, emission, reflectance);

	float distance = length(light_direction);
	float attenuationInv = constant + linear * distance +
	    exponent * distance * distance;

	//TODO: attenuationInv zero check
	return lightColor / attenuationInv;
}

vec3 diffuse(vec3 albedo, vec3 normal, vec3 toLightDirection, vec3 lightColor, float lightIntensity){
	float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
	return albedo * lightColor * diffuseFactor * lightIntensity;
}

vec3 specular(vec3 normal, vec3 position, vec3 toLightDir, vec3 lightColor, float reflectance, float emission, float lightIntensity){
	vec3 cameraDirection = normalize(-position);
	vec3 fromLightDir = -toLightDir;
	vec3 reflectedLight = normalize(reflect(fromLightDir, normal));
	float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
	specularFactor = pow(specularFactor, emission);
	return lightIntensity  * specularFactor * reflectance * lightColor;
}

vec3 calculateLight(vec3 albedo, vec3 position, vec3 normal, PointLight light, float reflectance, float emission){
	vec3 lightDirection = light.position.xyz - position;
	vec3 toLightDir  = normalize(lightDirection);

	vec3 diffuse = diffuse(albedo, normal, toLightDir, light.color.rgb, light.intensity);
	vec3 specular = specular(normal, position, toLightDir,light.color.rgb, reflectance, emission, light.intensity);

	// Attenuation
	float distance = length(light.position.xyz - position);
	float attenuation = 1.0 / (1.0 + (distance / light.radius));

	diffuse *= attenuation;
	specular *= attenuation;

	return diffuse + specular;
}

void main(void){

		vec3 finalColor = vec3(0);
		vec3 albedo = vec3(0);
		vec3 position = vec3(0);
		vec4 normal = vec4(0);
		vec4 specular_emission_diffuse_ssao_bloom = vec4(0);
		vec4 depth = vec4(0);

		albedo = texture(albedoSampler, texCoord).rgb;
		normal = texture(normalSampler, texCoord).rbga;

		position = texture(worldPositionSampler, texCoord).rgb;
		specular_emission_diffuse_ssao_bloom = texture(specular_emission_diffuse_ssao_bloom_Sampler, texCoord).rgba;

		// Locating which cluster this fragment is part of
			uint zTile = uint((log(abs(vec3(viewMatrix * vec4(position, 1.0)).z) / zNear) * gridSize.z) / log(zFar / zNear));
			vec2 tileSize = screenDimensions / gridSize.xy;

			uvec3 tile = uvec3(gl_FragCoord.xy / tileSize, zTile);
			uint tileIndex =
			        tile.x + (tile.y * gridSize.x) + (tile.z * gridSize.x * gridSize.y);

			uint lightCount = clusters[tileIndex].count;

			for (int i = 0; i < lightCount; ++i)
			{
			    uint lightIndex = clusters[tileIndex].lightIndices[i];
			    PointLight light = pointLight[lightIndex];
			    // TODO: do cool lighting


			    /*finalColor += calculatePointLight(albedo, position, normalize(normal.xyz),
			    		light, specular_emission_diffuse_ssao_bloom.r, specular_emission_diffuse_ssao_bloom.g);*/
			    finalColor += calculateLight(albedo, position, normalize(normal.xyz), light, specular_emission_diffuse_ssao_bloom.r, specular_emission_diffuse_ssao_bloom.g);

			}

			deferred_out = vec4(finalColor, 1.0);
}
