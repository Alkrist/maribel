#version 140

#gui_lib.glsl

uniform vec4 color;

uniform float borderRadius;
uniform float borderThickness;
uniform vec3 borderColor;

uniform vec2 framePositionPx;
uniform vec2 frameSizePx;


out vec4 out_Color;

void main(void){
	out_Color = color;

	vec4 borderPosition = getBorderPosition(framePositionPx, frameSizePx);

	if(isOverlappingCorner(gl_FragCoord.xy, borderPosition, borderRadius) == 1){
		discard;
	}

	if(isOverlappingCornerBorder(gl_FragCoord.xy, borderPosition, borderRadius, borderThickness) == 1
			|| isOverlappingBorder(gl_FragCoord.xy, borderPosition, borderThickness) == 1){
		out_Color = vec4(borderColor, 1.0);
	}

}
