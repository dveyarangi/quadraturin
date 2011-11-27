package yarangi.graphics.quadraturin.threads;

public interface IChainedThread {


	public String getName();
	
	public int getOrdial();
	public void setOrdial(int size);


	public void start();

	public void stop();

}
