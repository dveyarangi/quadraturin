package yarangi.spatial;

import javax.media.opengl.GL;

import yarangi.math.Vector2D;

public class PointQuadTree <T>
{
	private PointQuadNode <T> root;
	
	
	public PointQuadTree(Vector2D center)
	{
		root = new PointQuadNode<T>(center, null);
	}
	
	public PointQuadNode <T> getRoot() { return root; }
	
	public void add(Vector2D point, T object)
	{
//		System.out.println("adding");
//		System.exit(0);
		root.add(point, object);
	}
	
	public T remove(Vector2D point, T object)
	{
		return root.deletePoint(point, object, root, -1, -1);
	}
	

	public void updateLocation(Vector2D old, Vector2D loc, T object) {
		T o = remove(old, object);
		add(loc, o);
	}
	
	public void render(GL gl)
	{
		gl.glColor3f(1f,1f,1f);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex3f((float)root.getPoint().x(), -1000f,0f);
		gl.glVertex3f((float)root.getPoint().x(), 1000f,0f);
		gl.glEnd();
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex3f(-1000f, (float)root.getPoint().y(), 0f);
		gl.glVertex3f(1000f, (float)root.getPoint().y(), 0f);
		gl.glEnd();
		int nodes = 0;
		nodes += render(gl, root.getChild(PointQuadNode.NORTH, PointQuadNode.WEST), root, -1000, root.getPoint().y(), root.getPoint().x(), -1000);
		nodes += render(gl, root.getChild(PointQuadNode.NORTH, PointQuadNode.EAST), root, root.getPoint().x(), root.getPoint().y(), 1000, -1000);
		nodes += render(gl, root.getChild(PointQuadNode.SOUTH, PointQuadNode.WEST), root, -1000, 1000, root.getPoint().x(), root.getPoint().y());
		nodes += render(gl, root.getChild(PointQuadNode.SOUTH, PointQuadNode.EAST), root, root.getPoint().x(), 1000, 1000, root.getPoint().y());
//		System.out.println("Nodes in quad tree: " + nodes);
	}
	
	public int render(GL gl, PointQuadNode node, PointQuadNode parent, double bx1, double by1, double bx2, double by2)
	{
		if(node == null)
			return 0;
		Vector2D np = node.getPoint();
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex3f((float)np.x(), (float)by1, 0);
		gl.glVertex3f((float)np.x(), (float)by2, 0);
		gl.glEnd();
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex3f((float)bx1, (float)np.y(), 0);
		gl.glVertex3f((float)bx2, (float)np.y(), 0);
		gl.glEnd();
		int nodes = 1;
		nodes += render(gl, node.getChild(PointQuadNode.NORTH, PointQuadNode.WEST), node, bx1, np.y(), np.x(), by2);
		nodes += render(gl, node.getChild(PointQuadNode.NORTH, PointQuadNode.EAST), node, np.x(), np.y(), bx2, by2);
		nodes += render(gl, node.getChild(PointQuadNode.SOUTH, PointQuadNode.WEST), node, bx1, by2, np.x(), np.y());
		nodes += render(gl, node.getChild(PointQuadNode.SOUTH, PointQuadNode.EAST), node, np.x(), by1, bx2, np.y());
		
		return nodes;
	}

}
