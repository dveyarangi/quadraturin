package yarangi.spatial;

import yarangi.math.Vector2D;

public class PointQuadNode <T>
{
	public static final int NORTH = 1;
	public static final int SOUTH = 0;
	public static final int WEST = 0;
	public static final int EAST = 1;
	public static final int BUCKET_SIZE = 4;
	
	private Vector2D point;
	
	private T object;
	
	
//	private ArrayList <Vector2D> bucket = new ArrayList<Vector2D>(BUCKET_SIZE);
	
	@SuppressWarnings("unchecked")
	private PointQuadNode <T> [][] children = new PointQuadNode[2][2];
	
	public PointQuadNode(Vector2D point, T object)
	{

		this.point = point;
		this.object = object;
	}
	
	public Vector2D getPoint() { return point; }

	
	public PointQuadNode <T> add(Vector2D p, T object)
	{
		// calculating descent direction:
		int northing = point.y() > p.y() ? NORTH : SOUTH; 
		int easting = point.x() < p.x() ? EAST : WEST;
		
		
		PointQuadNode <T> node = children[northing][easting];
		
		if(node == null) // creating a new node:
			return (children[northing][easting] = new PointQuadNode <T>(p, object));
		
		// propagating to the child:
		return node.add(p, object);
	}
	

	
	public T deletePoint(Vector2D p, T object, PointQuadNode <T> parent, int parentNorthing, int parentEasting)
	{
//		System.out.println(this.object + " ::: " + object);
		if(this.object != object)
		{
			int northing = point.y() > p.y() ? NORTH : SOUTH; 
			int easting = point.x() < p.x() ? EAST : WEST;
			PointQuadNode <T> node = children[northing][easting];
			if (node == null)
			{
				System.out.println(this.object + " ::: " + object);
				System.out.println("removing - not found: ");
				return null;
			}
			return node.deletePoint(p, object, this, northing, easting);
		}
		
//		System.out.println("removed object found");
		
		// well...
		// now we are supposed to find the closest point to p...
		
		int nd = -1;
		int ed = -1;
		PointQuadNode <T> node = null;
		outter: for(nd = SOUTH; nd < NORTH; nd ++)
			for(ed = WEST; ed < EAST; ed ++)
				if((node = children[nd][ed]) != null)
					break outter;
		
		if(node == null) // no children, can be safely removed:
		{
			System.out.println("removing - no children");
			parent.children[parentNorthing][parentEasting] = null;
			return detachedObject();
		}	
		
		// node has children, descending to "random" direction 
		// to find suitable node for replacement:
		
		int newNorthing = nd == NORTH ? SOUTH : NORTH;
		int newEasting = ed == WEST ? EAST : WEST;
		
		if(node.children[newNorthing][newEasting] == null) 
		// direct child has no closer children, so if will be the replacement node
		// TODO: actually, it still can have closer children, re-implement it once we got proximity function
		{
			System.out.println("removing - replacer has no children");
			// replacing the removed node with the child:
			this.point = node.point;
			this.object = node.object;
			
			// the same descent direction as the replacement node remains the same:
			this.children[nd][ed] = node.children[nd][ed];
			
			// re-inserting the other children:
			this.addAll(node.children[nd][newEasting]);
			this.addAll(node.children[newNorthing][ed]);
			return detachedObject();
		}

		// if we got here, we need to descent further to find the closest replacement node:
		PointQuadNode <T> removalParent = node;

		while((node.children[newNorthing][newEasting]) != null)
		{
			removalParent = node;
			node = removalParent.children[newNorthing][newEasting];
		}
		
		this.point = node.point;
		this.object = node.object;
		removalParent.children[newNorthing][newEasting] = null;
		this.addAll(node.children[nd][newEasting]);
		this.addAll(node.children[newNorthing][ed]);
		this.addAll(node.children[nd][ed]);
		System.out.println("removing - replacer is making it hard");
		
		return detachedObject();
	}
	
	private T detachedObject() 
	{
		T object = this.object;
		this.object = null;
		return object;
	}
	
	
/*	public boolean updatePoint(Vector2D p, PointQuadNode parent, int parentNorthing, int parentEasting)
	{
		if(point.x != p.x && point.y != p.y)
		{
			int northing = point.x < p.x ? NORTH : SOUTH; 
			int easting = point.y < p.y ? WEST : EAST;
			PointQuadNode node = children[northing][easting];
			if (node == null)
				return false;
			return node.updatePoint(p, this, northing, easting);
		}
		
		
	}*/
	
	/**
	 * Traverses specified node and adds it and all it's children to this node.
	 * @param node
	 */
	private void addAll(PointQuadNode <T> node)
	{
		if(node == null)
			return;
		
		add(node.point, node.object);
		addAll(node.children[NORTH][WEST]);
		addAll(node.children[NORTH][EAST]);
		addAll(node.children[SOUTH][WEST]);
		addAll(node.children[SOUTH][EAST]);
	}

	public PointQuadNode getChild(int north2, int west2) {
		return children[north2][west2];
	}

}
