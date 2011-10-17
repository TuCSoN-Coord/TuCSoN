package alice.tucson.api;

public class TucsonOpId
{
	private long id;

	public TucsonOpId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public boolean equals(Object opid)
	{
		return id == ((TucsonOpId) opid).id;
	}

	public String toString()
	{
		return "" + id;
	}
}
