#version 430

layout(triangles, invocations = 6) in;

layout(triangle_strip, max_vertices = 3) out;

uniform mat4 modelMatrix;
uniform mat4 projectionViewMatrices[3];

void main() {

	gl_Layer = gl_InvocationID;
	gl_Position = projectionViewMatrices[gl_InvocationID] * modelMatrix * gl_Position;

	EndPrimitive();
}
