package yarangi.graphics.quadraturin.threads;

public interface IChainedThread {


	public String getName();
	
	/**
	 * TODO: create a counter in ThreadChain instead of this property?
	 */
	public int getOrdial();
	public void setOrdial(int size);


	public void start();

	public void stop();

}
