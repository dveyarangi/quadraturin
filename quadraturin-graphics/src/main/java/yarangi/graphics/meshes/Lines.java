package yarangi.graphics.meshes;

import java.awt.Color;

import javax.media.opengl.GL;

import yarangi.math.Angles;

public class Lines 
{

	public static void drawGlowingLine(GL gl, double x1, double y1, double x2, double y2, double lineWidth, double glowWidth, Color color)
	{
		gl.glPushMatrix();
		
		gl.glTranslatef((float)x1, (float)y1, 0);
		gl.glRotatef((float)Angles.toDegrees(Math.atan2(y2-y1, x2-x1)), 0, 0, 1);
		float halfWidth = (float)(lineWidth / 2.);
		float halfGlow = (float)(glowWidth / 2.);
		float xOffset = (float)(x2-x1);
		
		float a = (float)(color.getAlpha()/255.); 
		float r = 0xFF & (color.getRGB() << 16);
		float g = 0xFF & (color.getRGB() << 8);
		float b = 0xFF & (color.getRGB());
		gl.glBegin(GL.GL_QUADS);
		gl.glColor4f(r,g,b,a);
		gl.glVertex3f(0,-halfWidth, 0);
		gl.glVertex3f(0,halfWidth, 0);
		gl.glVertex3f(xOffset, halfWidth, 0);
		gl.glVertex3f(xOffset, -halfWidth, 0);
		
		// left side
		gl.glVertex3f(0,-halfWidth, 0);
		gl.glVertex3f(xOffset, -halfWidth, 0);
		
		gl.glColor4f(r,g,b,0f);
		gl.glVertex3f(xOffset, -halfGlow, 0);
		gl.glVertex3f(0, -halfGlow,  0);
		
		// right side
		gl.glColor4f(r,g,b,a);
		gl.glVertex3f(0,halfWidth, 0);
		gl.glVertex3f(xOffset, halfWidth, 0);
		
		gl.glColor4f(r,g,b, 0f);
		gl.glVertex3f(xOffset, halfGlow, 0);
		gl.glVertex3f(0,halfGlow, 0);
		gl.glEnd();
		
		gl.glPopMatrix();
	}
}