package ai;

import java.util.List;

import common.Vectorf2;

public class Node
{
	private List<Edge> edges;

	private Vectorf2 position;

	public List<Edge> getEdges()
	{
		return edges;
	}

	public Vectorf2 getPosition()
	{
		return position;
	}

	public void setEdges(List<Edge> edges)
	{
		this.edges = edges;
	}

	public void setPosition(Vectorf2 position)
	{
		this.position = position;
	}
}
