#version 330

in vec2 texCoord_fs;

layout(location = 0) out vec4 fragColor;

uniform sampler2D texture;

void main(void) {
	vec4 rgba = texture2D(texture, texCoord_fs);

	if (rgba.a < 1.0){
		discard;
	}

	vec3 albedo = rgba.rgb;
	albedo = pow(albedo, vec3(2.2)); // sRGB -> linear
	fragColor = vec4(albedo, 1.0);
}
