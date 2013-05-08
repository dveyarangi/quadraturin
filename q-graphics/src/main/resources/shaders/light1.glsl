[fragment_shader]
uniform sampler2D sceneTex; // 0
uniform vec4 color;
uniform float height;
uniform float cutoff;
void main()
{
    vec4 coord = gl_TexCoord[0];
    vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;
    float distance = sqrt(sqrt(pow(1.0-2.0*coord.x, 2.0) + pow(1.0-2.0*coord.y, 2.0)));

    // 1/distance with some renormalization. won't solve quantum gravity issues at high energies, though.
	float param = (1.0-tc.r) * (1.0 / (distance*cutoff) - 1.0/cutoff);
      
    gl_FragColor = vec4(color.r * param, color.g * param, color.b * param, 0);
}