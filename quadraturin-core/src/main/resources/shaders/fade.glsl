[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform float decay; // fadeout step
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	float tc0 = decay; if(tc.r <= decay) tc0 = tc.r;
	float tc1 = decay; if(tc.g <= decay) tc1 = tc.g;
	float tc2 = decay; if(tc.b <= decay) tc2 = tc.b;
	float tc3 = decay; if(tc.a <= decay) tc3 = tc.a;
	
    gl_FragColor = tc - vec4(tc0, tc1, tc2, tc3);

}
