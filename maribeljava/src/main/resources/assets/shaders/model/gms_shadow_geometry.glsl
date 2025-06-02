#version 430

layout(triangles, invocations = 5) in;

layout(triangle_strip, max_vertices = 3) out;

uniform mat4 modelMatrix;

layout (std140, row_major) uniform LightViewProjections{
	mat4 m_lightViewProjection[3];
};

void main() {

	for(int i = 0; i < 5; i++){
		gl_Position = m_lightViewProjection[gl_InvocationID] * modelMatrix * gl_in[i].gl_Position;
		gl_Layer = gl_InvocationID;
		EmitVertex();
	}

	EndPrimitive();
}
