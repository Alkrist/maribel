#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;
layout (location=2) in vec2 textureCoord;

out vec2 FS_textureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;


void main(void){
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    FS_textureCoord = textureCoord;
}
