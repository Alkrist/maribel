#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec2 textureCoords_FS;

void main(void){
		mat4 viewModelMatrix = viewMatrix * modelMatrix;
		vec4 outWorldPosition = modelMatrix * vec4(position, 1.0);
		vec4 outViewPosition  = viewMatrix * outWorldPosition;

		gl_Position   = projectionMatrix * outViewPosition;

		textureCoords_FS = textureCoords;
}
