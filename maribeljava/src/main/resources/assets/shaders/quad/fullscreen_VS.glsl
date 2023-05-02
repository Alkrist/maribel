#version 430

layout (location = 0) in vec3 position;
//layout (location = 1) in vec2 texCoords;

out vec2 textureCoords;
void main()
{
	gl_Position = vec4(position,1.0);
	textureCoords = vec2(1 - (position.x+1.0)/2.0, (position.y+1.0)/2.0);
}
