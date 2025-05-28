#version 330

in vec2 texCoord_fs;

layout(location = 0) out vec4 fragColor;

uniform sampler2D texture;
uniform vec4 color;

void main(void) {

	vec4 rgba = texture2D(texture, texCoord_fs);

	/*if (rgba.a < 1.0){
		discard;
	}*/

	fragColor = color;
}
