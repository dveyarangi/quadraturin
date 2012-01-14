
[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform float decay; // fadeout step
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	// TODO: leaves traces when alpha decays faster that other color 
	// guess the shader ignores zero alpha pixels
	float tc0, tc1, tc2, tc3;
	if(tc.r >= decay) tc0 = tc.r-decay; else tc0=0;
	if(tc.g >= decay) tc1 = tc.g-decay; else tc1=0;
	if(tc.b >= decay) tc2 = tc.b-decay; else tc2=0;
	if(tc.a >= decay) tc3 = tc.a-decay; else tc3=0;
	
	
    gl_FragColor =  vec4(tc0, tc1, tc2, tc3);

}
