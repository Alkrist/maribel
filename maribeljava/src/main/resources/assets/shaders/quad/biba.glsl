#version 430

layout(triangles, invocations = 1) in;

layout(triangle_strip, max_vertices = 3) out;

in vec2 texCoords[];
out vec2 textureCoords;

void main() {

	for(int i = 0; i<gl_in.length(); i++){
		textureCoords = texCoords[i];
		gl_Layer = gl_InvocationID;
		EmitVertex();
	}

	EndPrimitive();


}
