

[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform vec4 color;

uniform float height;
uniform float cutoff;
void main()
{
    vec4 coord = gl_TexCoord[0];
    vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;

    float distance = sqrt(sqrt(pow(1-2*coord.x, 2) + pow(1-2*coord.y, 2)));

	float param = (1-tc.r) * (1 / (distance*cutoff) - 1/cutoff);
      
    gl_FragColor = vec4(color.r * param, color.g * param, color.b * param, 0);
}