const float ZFAR = 10000.0;


struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct AmbientLight
{
	vec3 color;
	float intensity;
};

struct DirectionLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct PointLight
{
    vec3 position;
    vec3 color;
    float intensity;
    Attenuation att;
};

struct SpotLight
{
	PointLight pl;
	vec3 conedir;
	float cutoff;
};


float calculateFogFactor(float dist, float sightRangeFactor) {
	return smoothstep(0, 1, -0.0002 / sightRangeFactor * (dist - (ZFAR) / 10 * sightRangeFactor) + 1);
}

vec3 calculateLightColor(vec3 normal, vec3 position, vec3 to_light_dir, vec3 lightColor, float light_intensity, float emission, float reflectance){

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
	return (diffuseColor + specularColor);
}

vec3 calculateAmbientLight(AmbientLight light, float emission) {
    return light.color * light.intensity * emission;
}

vec3 calculateDirectionalLight(vec3 position, vec3 normal, DirectionLight light, float reflectance, float emission){
	return calculateLightColor(normal, position, normalize(light.direction),
			light.color, light.intensity, emission, reflectance);
}

vec3 calculatePointLight(vec3 position, vec3 normal, PointLight light, float reflectance, float emission){
	vec3 light_direction = light.position - position;
	vec3 to_light_dir  = normalize(light_direction);
	vec3 lightColor = calculateLightColor(normal, position, to_light_dir,
			light.color, light.intensity, emission, reflectance);

	float distance = length(light_direction);
	float attenuationInv = light.att.constant + light.att.linear * distance +
	    light.att.exponent * distance * distance;

	return (attenuationInv != 0.0) ? lightColor / attenuationInv : lightColor;
}

vec3 calculateSpotLight(vec3 position, vec3 normal, SpotLight light, float reflectance, float emission){
	 vec3 light_direction = light.pl.position - position;
	 vec3 to_light_dir  = normalize(light_direction);
	 vec3 from_light_dir  = -to_light_dir;
	 float spot_alfa = dot(from_light_dir, normalize(light.conedir));

	 vec3 lightColor = vec3(0);
	 if (spot_alfa > light.cutoff){
		 lightColor = calculatePointLight(position, normal, light.pl, reflectance, emission);
		 lightColor *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
	 }

	 return lightColor;
}

//TODO: shadow mapping functions

