#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 frameColor;
uniform float hasTexture;

void main(void){

	if(hasTexture == 1.0){
		vec4 texturedColor = texture(guiTexture,textureCoords);
		vec3 finalColor = mix(texturedColor.xyz, frameColor.xyz, frameColor.a);
		out_Color = vec4(finalColor, texturedColor.a);
	}else{
		out_Color = frameColor;
	}
}