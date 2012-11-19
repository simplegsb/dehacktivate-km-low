package ai;

public class Edge
{
	private Node destination;

	private float distance;

	private Node source;

	public Node getDestination()
	{
		return destination;
	}

	public Node getSource()
	{
		return source;
	}

	public float getDistance()
	{
		return distance;
	}

	public void setDestination(Node destination)
	{
		this.destination = destination;
	}

	public void setSource(Node source)
	{
		this.source = source;
	}

	public void setDistance(float distance)
	{
		this.distance = distance;
	}
}
