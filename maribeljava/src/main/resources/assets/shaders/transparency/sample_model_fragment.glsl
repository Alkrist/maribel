#version 430 core

in vec2 textureCoords_FS;

layout(location = 0) out vec4 albedo_out;
layout(location = 1) out vec4 alpha_out;
uniform sampler2D diffusemap;

void main(void){
	//ALBEDO
	vec4 albedo = texture(diffusemap, textureCoords_FS);
	albedo_out = vec4(albedo.rgb, albedo.a);
	alpha_out = vec4(albedo.a, 0, 0, 1);
}
