

[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform vec4 color;
void main()
{
  
  	vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

	float distance = (0.5-gl_TexCoord[0].x)* (0.5-gl_TexCoord[0].x) + (0.5-gl_TexCoord[0].y)*(0.5-gl_TexCoord[0].y);
	float param = 1*exp(-distance*20);
	float tc0 = (1-tc.r) * color.r * param; 
	float tc1 = (1-tc.r) * color.g * param; 
	float tc2 = (1-tc.r) * color.b * param; 
	float tc3 = 0;//(tc.r) * color.a * param; 
    gl_FragColor = vec4(tc0, tc1, tc2, tc3);
}
