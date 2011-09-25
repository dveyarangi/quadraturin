package yarangi.spatial;

import java.util.List;

public interface IGridListener <K>
{
	public void cellsModified(final List <K> cells);
	
}
