#version 330

in vec2 texCoord_fs;

layout(location = 0) out vec4 fragColor;

uniform sampler2D texture;
uniform vec3 primaryColor;

//Smooth edge parameters
const float width = 0.5;
const float edge = 0.05;

//Border parameters
uniform float borderWidth;
uniform float borderEdge;

//Outline parameters
uniform vec3 outlineColor;
uniform vec2 outlineOffset;

void main(void) {

	float distance = 1.0 - texture(texture, texCoord_fs).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	float distance2 = 1.0 - texture(texture, texCoord_fs + outlineOffset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

	float totalAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 totalColor = mix(outlineColor, primaryColor, alpha / totalAlpha);
	fragColor = vec4(totalColor, totalAlpha);
}
