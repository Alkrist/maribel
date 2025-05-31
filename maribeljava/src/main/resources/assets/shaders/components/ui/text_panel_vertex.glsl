#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

out vec2 texCoord_fs;

uniform mat4 orthographicMatrix;

void main(void) {
	vec2 translation = vec2(0);
	mat4 o = orthographicMatrix;
	gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
	texCoord_fs = texCoord;
}
