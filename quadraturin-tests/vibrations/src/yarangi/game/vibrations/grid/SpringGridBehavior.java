package yarangi.game.vibrations.grid;

import yarangi.graphics.quadraturin.objects.Behavior;
import yarangi.math.Vector2D;
import yarangi.numbers.RandomUtil;
import yarangi.spatial.Tile;

public class SpringGridBehavior implements Behavior <SpringGrid>
{

	public final static double SPRING_K = 50;
	public final static double JOINT_K = 20;
	
	public double gridTime = 0;
	public double lastPull = -PULL_INTERVAL;
	public boolean pullOut = true;
	
	public final static double PULL_INTERVAL = 0.01;
	
	private double [] rx, ry;
	
	@Override
	public boolean behave(double time, SpringGrid entity, boolean isVisible)
	{
		
		Tile <Joint> tile;
		Vector2D curr;
		Vector2D temp;
		Tile <Joint> tempTile;
		double length, coef;
		for(int x = 0; x < entity.getGridWidth(); x ++)
			for(int y = 0; y < entity.getGridHeight(); y ++)
			{
				tile = entity.getTile( x, y );
				curr = tile.get().plus( tile.getX(), tile.getY());
				
//				System.out.println("-------");
				Vector2D force = Vector2D.ZERO();
				if(x > 0)
				{
					tempTile = entity.getTile( x-1, y );
					temp = tempTile.get().plus( tempTile.getX(), tempTile.getY() ).minus( curr );
					length = temp.abs();
					coef = (length - entity.getCellSize())* SPRING_K / length;
					force.add( temp.x() * coef, temp.y() * coef );
				}
				if(x < entity.getGridWidth()-1)
				{
					tempTile = entity.getTile( x+1, y );
					temp = tempTile.get().plus( tempTile.getX(), tempTile.getY() ).minus( curr );
					length = temp.abs();
					coef = (length - entity.getCellSize())* SPRING_K / length;
					force.add( temp.x() * coef, temp.y() * coef );
				}
				if(y > 0)
				{
					tempTile = entity.getTile( x, y-1 );
					temp = tempTile.get().plus( tempTile.getX(), tempTile.getY() ).minus( curr );
					length = temp.abs();
					coef = (length - entity.getCellSize())* SPRING_K / length;
					force.add( temp.x() * coef, temp.y() * coef );
				}
				if(y < entity.getGridHeight()-1)
				{
					tempTile = entity.getTile( x, y+1 );
					temp = tempTile.get().plus( tempTile.getX(), tempTile.getY() ).minus( curr );
					length = temp.abs();
					coef = (length - entity.getCellSize()) * SPRING_K / length;
					force.add( temp.x() * coef, temp.y() * coef );
				}
				
				length = tile.get().abs();
				coef = JOINT_K * time;
				force.add( -tile.get().x() * coef, -tile.get().y() * coef );
//				System.out.println("force: " + force);
//				System.out.println(time);
				double ax = force.x() / 1;
				double ay = force.y() / 1;
				double vx = ax * time;
				double vy = ay * time;
//				if(vx != 0 || vy != 0)
//				System.out.println(x + ":" + y + " - " + vx + " : " + vy);
				tile.get().add( vx*time, vy * time );
				tile.get().setVelocity( vx, vy );
			}
//		System.out.println(time);
		
		gridTime += time;
		double r;
		if(gridTime - lastPull > PULL_INTERVAL)
		{
			if(rx == null)
			{
				rx = new double [entity.getGridWidth()];
				ry = new double [entity.getGridHeight()];
				for(int y = 0; y < entity.getGridHeight(); y ++)
					ry[y] = /*10;//*/RandomUtil.STD( 10, 10 );
				for(int x = 0; x < entity.getGridWidth(); x ++)
					rx[x] = /*10;//*/RandomUtil.STD( 10, 10 );
			}
			for(int y = 0; y < entity.getGridHeight(); y ++)
			{
				if(y % 2 == 0)
					continue;
				
//				r = ry[y];
				r = RandomUtil.STD( 0, 10 );
				
				entity.getTile( 0, y ).get().setxy( r, r);
				entity.getTile( entity.getGridHeight()-1, y ).get().setxy( r, r);
			}
			for(int x = 0; x < entity.getGridWidth(); x ++)
			{
				if(x % 2 == 0)
					continue;
				//r = rx[x];
//				r = RandomUtil.getRandomGaussian( 10, 10 );
				r = RandomUtil.STD( 0, 10 );
				entity.getTile( x, 0 ).get().setxy( r, r);
				entity.getTile( x, entity.getGridHeight()-1 ).get().setxy( r, r);
			}
			lastPull = gridTime;
			pullOut = !pullOut;
		}
		return false;
	}


}
