[vertex_shader]
void main(void)
{
  gl_Position = ftransform();
  gl_TexCoord[0] = gl_MultiTexCoord0;
}

[fragment_shader]

uniform float rt_w; // render target width

uniform sampler2D sceneTex; // 0
//float offset[5] = float[]( 0.0, 1.0, 2.0, 3.0, 4.0 );
//float weight[5] = float[]( 0.2270270270, 0.1945945946, 0.1216216216, 0.0540540541, 0.0162162162 );
float offset[3] = float[]( 0.0, 1.3846153846, 3.2307692308 );
float weight[3] = float[]( 0.2270270270, 0.3162162162, 0.0702702703 );

void main()
{
  vec4 tc = vec4(0.0, 0.0, 0.0, 0.0);
//  if (gl_TexCoord[0].x<(vx_offset-0.01))
 // {
    vec2 uv = gl_TexCoord[0].xy;
    tc = texture2D(sceneTex, uv).rgba * weight[0];
  
     for (int i=1; i<3; i++)
    {
       tc += texture2D( sceneTex, uv + vec2(offset[i])/rt_w, 0.0 ).rgba * weight[i];
       tc += texture2D( sceneTex, uv - vec2(offset[i])/rt_w, 0.0 ).rgba * weight[i];
    }

    gl_FragColor = tc;
}
