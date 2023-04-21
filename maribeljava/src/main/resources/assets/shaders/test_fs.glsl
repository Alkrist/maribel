#version 400 core

in vec2 textureCoords_FS;

uniform sampler2D modelTexture;

out vec4 out_Color;

void main(void){

	//vec4 biba = texture(modelTexture, textureCoords_FS);
	//out_Color = vec4(1,1,1,1);
	out_Color = texture(modelTexture, textureCoords_FS);

}
