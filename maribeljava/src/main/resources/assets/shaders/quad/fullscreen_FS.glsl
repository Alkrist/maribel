#version 330

in vec2 textureCoords;
layout(location = 0) out vec4 out_Color;


uniform sampler2D texture;

void main()
{
	vec4 rgba = texture2D(texture, textureCoords);

	if (rgba.a == 0)
		discard;

	out_Color = rgba;
}
