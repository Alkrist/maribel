#version 400 core

in vec3 position;
in vec2 texCoords;

out vec2 pass_texCoords;

void main(void){
	gl_Position = vec4(position,1.0);
	pass_texCoords = texCoords;
}