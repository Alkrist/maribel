#version 330

in vec2 FS_textureCoord;

layout(location = 0) out vec4 albedo_out;

uniform sampler2D textureSampler;

void main(void){
	vec3 albedo = texture(textureSampler, FS_textureCoord).rgb;
	albedo = pow(albedo, vec3(2.2)); // sRGB -> linear

	albedo_out = vec4(albedo, 0);
}
