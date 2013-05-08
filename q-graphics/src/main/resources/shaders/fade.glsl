
[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform float decay; // fadeout step
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	// TODO: leaves traces when alpha decays slower that other color 
	// guess the shader ignores zero alpha pixels
	float tcr = tc.r - decay;
	float tcg = tc.g - decay;
	float tcb = tc.b - decay; 
	float tca = tc.a - decay;
	
	if(tcr < 0.0) tcr = 0.0;
	if(tcg < 0.0) tcg = 0.0;
	if(tcb < 0.0) tcb = 0.0;
	if(tca < 0.0) tca = 0.0;
	
	
    gl_FragColor =  vec4(tcr, tcg, tcb, tca);

}
