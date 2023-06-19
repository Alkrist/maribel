#version 140

in vec2 position;

uniform mat4 transformationMatrix;

void main(void){

	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);

}
