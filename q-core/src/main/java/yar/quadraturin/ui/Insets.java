package yar.quadraturin.ui;

public class Insets
{
	public static final Insets ZERO = new Insets(0,0,0,0);
	private int left, right, top, bottom;

	public Insets(int left, int right, int top, int bottom)
	{
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	protected int getLeft()
	{
		return left;
	}

	protected int getRight()
	{
		return right;
	}

	protected int getTop()
	{
		return top;
	}

	protected int getBottom()
	{
		return bottom;
	}
	
	
}
