[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform vec4 min; // minimal included value
uniform vec4 max; // maximal included value
uniform vec4 target; // maximal included value
uniform vec4 overflow; // maximal included value
uniform vec4 underflow; // maximal included value
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	vec4 res;
	float offset = 0.3;
	for(int i = 0; i < 4; i ++) {
		if(tc[i] < min[i]) { res[i] = underflow[i]; }
		else 
		if(tc[i] > max[i]) { res[i] = overflow[i]; } 
		else {
			res[i] = target[i]; }
	}
		
//	if(tc.r < min[0]) tc0 *= offset-(min[0]-tc0)*(min[0]-tc0);
//	if(tc.r > max[0]) tc0 *= offset-(tc0-max[0])*(tc0-max[0]);
//	if(tc.g < min[1]) tc1 *= offset-(min[1]-tc1)*(min[1]-tc1);
//	if(tc.g > max[1]) tc1 *= offset-(tc1-max[1])*(tc1-max[1]);
//	if(tc.b < min[2]) tc2 *= offset-(min[2]-tc2)*(min[2]-tc2);
//	if(tc.b > max[2]) tc2 *= offset-(tc2-max[2])*(tc2-max[2]);
//	if(tc.a < min[3]) tc3 *= offset-(min[3]-tc3)*(min[3]-tc3);
//	if(tc.a > max[3]) tc3 *= offset-(tc3-max[3])*(tc3-max[3]);
//	if(tc.g < min[1] || tc.g > max[1]) tc1 = 0;
//	if(tc.b < min[2] || tc.b > max[2]) tc2 = 0;
//	if(tc.a < min[3] || tc.a > max[3]) tc3 = 0;
	gl_FragColor=res;
		
//		gl_FragColor=target;


}