package yarangi.graphics.quadraturin.samples.terrain;

import yarangi.graphics.quadraturin.Swing2DContainer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int width = 400;
		int height = 200;
		
		GraphScene model = new GraphScene();
		
		Swing2DContainer frame = new Swing2DContainer();
		int id = frame.addScene(model);

		frame.start();
		
		frame.activateScene(id);

	}

}
