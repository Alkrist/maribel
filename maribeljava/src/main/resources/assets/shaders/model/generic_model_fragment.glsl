#version 430 core

in vec2 textureCoords_FS;
in vec3 position_FS;
in vec3 normal_FS;
in vec3 tangent_FS;
in vec3 bitangent_FS;

layout(location = 0) out vec4 albedo_out;
layout(location = 1) out vec4 worldPosition_out;
layout(location = 2) out vec4 normal_out;
layout(location = 3) out vec4 specularEmission_out;

struct Material
{
	sampler2D diffusemap;
	sampler2D normalmap;
	float shininess;
	float emission;
};

uniform Material material;


void main(void){

	//NORMAL MAP
	mat3 TBN = mat3(tangent_FS, bitangent_FS, normal_FS);
	vec3 normal = normalize(2*(texture(material.normalmap, textureCoords_FS).rgb)-1);
	normal = normalize(TBN * normal);


	//ALBEDO
	vec3 albedo = texture(material.diffusemap, textureCoords_FS).rgb;

	//FINAL
	albedo_out = vec4(albedo,1);
	worldPosition_out = vec4(position_FS,1);
	normal_out = vec4(normal.xyz,1);
	specularEmission_out = vec4(material.shininess, material.emission, 11, 1);
}
