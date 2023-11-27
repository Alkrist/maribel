#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform float hasTexture;
uniform vec4 frameColor;

//border properties
uniform vec4 borderColor;
uniform float borderSize;

//frame properties
uniform vec2 framePixelPosition;
uniform vec2 framePixelSize;

//border radius
uniform float cornerRadius;

void main(void) {

	if (hasTexture == 1.0) {
		vec4 texturedColor = texture(guiTexture, textureCoords);
		vec3 finalColor = mix(texturedColor.xyz, frameColor.xyz, frameColor.a);
		out_Color = vec4(finalColor, texturedColor.a);
	} else {
		out_Color = frameColor;

		//calculate border
		float leftBorder = framePixelPosition.x - framePixelSize.x / 2;
		float rightBorder = framePixelPosition.x + framePixelSize.x / 2;
		float bottomBorder = framePixelPosition.y - framePixelSize.y / 2;
		float topBorder = framePixelPosition.y + framePixelSize.y / 2;

		//find out if it is inside border zone
		if (gl_FragCoord.x < leftBorder + borderSize
				|| gl_FragCoord.x > rightBorder - borderSize
				|| gl_FragCoord.y < bottomBorder + borderSize
				|| gl_FragCoord.y > topBorder - borderSize) {
			out_Color = borderColor;
		}


		float dist = 0;
		float distToBorder = 0;
		if (gl_FragCoord.x < leftBorder + cornerRadius
				&& gl_FragCoord.y < bottomBorder + cornerRadius) {
			dist = distance(gl_FragCoord.xy, vec2(leftBorder + cornerRadius, bottomBorder + cornerRadius));

		} else if (gl_FragCoord.x < leftBorder + cornerRadius
				&& gl_FragCoord.y > topBorder - cornerRadius) {
			dist = distance(gl_FragCoord.xy, vec2(leftBorder + cornerRadius, topBorder - cornerRadius));

		} else if (gl_FragCoord.x > rightBorder - cornerRadius
				&& gl_FragCoord.y < bottomBorder + cornerRadius) {
			dist = distance(gl_FragCoord.xy, vec2(rightBorder - cornerRadius, bottomBorder + cornerRadius));

		} else if (gl_FragCoord.x > rightBorder - cornerRadius
				&& gl_FragCoord.y > topBorder - cornerRadius) {
			dist = distance(gl_FragCoord.xy, vec2(rightBorder - cornerRadius, topBorder - cornerRadius));

		}

		if (gl_FragCoord.x < leftBorder + cornerRadius + borderSize
						&& gl_FragCoord.y < bottomBorder + cornerRadius + borderSize){
			distToBorder = distance(gl_FragCoord.xy, vec2(leftBorder + cornerRadius + borderSize,
								bottomBorder + cornerRadius + borderSize));

		}else if (gl_FragCoord.x < leftBorder + cornerRadius + borderSize
				&& gl_FragCoord.y > topBorder - cornerRadius - borderSize){
			distToBorder = distance(gl_FragCoord.xy, vec2(leftBorder + cornerRadius + borderSize,
									topBorder - cornerRadius - borderSize));
		}else if (gl_FragCoord.x > rightBorder - cornerRadius - borderSize
				&& gl_FragCoord.y < bottomBorder + cornerRadius + borderSize){
			distToBorder = distance(gl_FragCoord.xy, vec2(rightBorder - cornerRadius - borderSize,
											bottomBorder + cornerRadius + borderSize));
		}else if (gl_FragCoord.x > rightBorder - cornerRadius - borderSize
				&& gl_FragCoord.y > topBorder - cornerRadius - borderSize){
			distToBorder = distance(gl_FragCoord.xy, vec2(rightBorder - cornerRadius - borderSize,
									topBorder - cornerRadius - borderSize));
		}

		if (dist > cornerRadius) {
			discard;
		}else if(distToBorder > cornerRadius){
			out_Color = borderColor;
		}


	}
}
