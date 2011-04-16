

[fragment_shader]

uniform sampler2D sceneTex; // 0
uniform vec4 color;

uniform float size;
uniform float height;
uniform float cutoff;
void main()
{
    vec4 coord = gl_TexCoord[0];
    vec4 tc = texture2D(sceneTex, gl_TexCoord[0].xy).rgba;
    

    float distance = sqrt(pow(0.5-coord.x, 2) + pow(0.5-coord.y, 2));
//  float a = atan(height/distance);
//  float param = 1; //(1-tc.r) * 1/ cos(a) * 1.02 - 0.02;
	float param = (1-tc.r) * (1 / (2*distance*cutoff+(1-size)/cutoff) - 1/cutoff);
      
    if(param > 1)
     	param = 1;
 
    gl_FragColor = vec4(color.r * param, color.g * param, color.b * param, 0);
}