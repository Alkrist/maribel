#version 330

layout (location = 0) in vec3 position;

uniform mat4 orthographicMatrix;

void main(void){
	gl_Position = orthographicMatrix * vec4(position, 1);
}
