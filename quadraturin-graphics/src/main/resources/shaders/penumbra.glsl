[fragment_shader]

uniform sampler2D sceneTex; // 0
void main()
{
	vec4 coord = gl_TexCoord[0];
	float sum = atan((coord.y)/(coord.x));

	// pixel angle ranges from 0 to pi/2	
	gl_FragColor = vec4(3.14159/2-2*sum,0,0,0);
}
