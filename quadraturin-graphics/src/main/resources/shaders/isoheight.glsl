[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform vec4 min; // minimal included value
uniform vec4 max; // maximal included value
uniform vec4 target; // maximal included value
void main()
{
	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	float tc0 = target.r;
	float tc1 = target.g;
	float tc2 = target.b;
	float tc3 = target.a;
	if(tc.r < min[0] || tc.r > max[0]) tc0 = 0;
	if(tc.g < min[1] || tc.g > max[1]) tc1 = 0;
	if(tc.b < min[2] || tc.b > max[2]) tc2 = 0;
	if(tc.a < min[3] || tc.a > max[3]) tc3 = 0;
	gl_FragColor=vec4(tc0,tc1,tc2,tc3);
		
//		gl_FragColor=target;


}