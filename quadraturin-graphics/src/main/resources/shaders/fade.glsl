
[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform float decay; // fadeout step
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	float tc0 = tc.r-decay; if(tc0 < 0.01) tc0 = 0;
	float tc1 = tc.g-decay; if(tc1 < 0.01) tc1 = 0;
	float tc2 = tc.b-decay; if(tc2 < 0.01) tc2 = 0;
	float tc3 = tc.a-decay; if(tc3 < 0.01) tc3 = 0;
	
    gl_FragColor =  vec4(tc0, tc1, 0, tc3);

}
