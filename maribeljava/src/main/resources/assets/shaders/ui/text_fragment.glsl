#version 330

in vec2 textureCoordsOUT;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

//TODO: make it uniforms to set it accordingly relative to font size

//Smooth edge parameters
uniform float width;
uniform float edge;

//Border parameters
uniform float borderWidth;
uniform float borderEdge;

//Outline parameters
uniform vec3 outlineColor;
uniform vec2 offset;

void main(void){

	float distance = 1.0 - texture(fontAtlas, textureCoordsOUT).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	float distance2 = 1.0 - texture(fontAtlas, textureCoordsOUT + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

	float totalAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 totalColor = mix(outlineColor, color, alpha / totalAlpha);
	out_color = vec4(totalColor, totalAlpha);

}
