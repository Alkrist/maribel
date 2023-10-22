#version 140

in vec2 textureCoords_FS;

out vec4 out_Color;

uniform sampler2D uiTexture;

void main(void){
	vec4 color = texture(uiTexture, textureCoords_FS);

	/*if(color.a < 0.5){
		discard;
	}*/

	out_Color = color;
}
