package yarangi.spatial;

import java.util.Collection;

public interface IGridListener <K>
{
	public void cellsModified(final Collection <K> cells);
	
}
