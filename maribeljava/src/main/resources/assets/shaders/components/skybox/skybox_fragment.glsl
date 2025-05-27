#version 330

in vec2 FS_textureCoord;
out vec4 fragColor;

uniform sampler2D textureSampler;

void main(void){
	vec3 albedo = texture(textureSampler, FS_textureCoord).rgb;
	albedo = pow(albedo, vec3(2.2)); // sRGB -> linear
	fragColor = vec4(albedo, 1.0);
}
