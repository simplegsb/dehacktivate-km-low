package ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.Vectorf2;

public class AStarPathFinder
{
	private List<Node> closedNodes;

	private List<Node> openNodes;

	private Map<Node, Node> previousNodes;

	private Map<Node, Float> totalDistances;

	private Map<Node, Float> travelledDistances;

	public AStarPathFinder()
	{
		closedNodes = new ArrayList<Node>();
		openNodes = new ArrayList<Node>();
		previousNodes = new HashMap<Node, Node>();
		totalDistances = new HashMap<Node, Float>();
		travelledDistances = new HashMap<Node, Float>();
	}

	private float computeHeuristic(Node node, Node finish)
	{
		return Vectorf2.subtract(node.getPosition(), node.getPosition()).getMagnitude();
	}

	private Node getClosestOpenNode()
	{
		float distance = 0;
		Node node = null;

		for (Entry<Node, Float> entry : totalDistances.entrySet())
		{
			if (openNodes.contains(entry.getKey()) && (node == null || entry.getValue() < distance))
			{
				distance = entry.getValue();
				node = entry.getKey();
			}
		}

		return node;
	}

	public List<Node> findPath(Node start, Node finish)
	{
		openNodes.add(start);

		travelledDistances.put(start, 0.0f);
		totalDistances.put(start, computeHeuristic(start, finish));
	 
		while (!openNodes.isEmpty())
		{
			Node node = getClosestOpenNode();
			if (node == finish)
			{
				return reconstructPath(start, finish);
			}
	 
			openNodes.remove(node);
			closedNodes.add(node);

			for (Edge edge : node.getEdges())
			{
				Node destination = edge.getDestination();

				if (closedNodes.contains(destination))
				{
	                 continue;
				}

				float distanceTravelled = travelledDistances.get(node) + edge.getDistance();
	 
				if (!openNodes.contains(destination) || distanceTravelled < travelledDistances.get(destination))
				{
					if (!openNodes.contains(destination))
					{
	                     openNodes.add(destination);
					}

					previousNodes.put(destination, node);
					travelledDistances.put(destination, distanceTravelled);
					totalDistances.put(destination, distanceTravelled + computeHeuristic(destination, finish));
				}
			}
		}

		return new ArrayList<Node>();
	}

	private List<Node> reconstructPath(Node start, Node finish)
	{
		List<Node> path = new ArrayList<Node>();
		path.add(finish);

		Node node = finish;
		while (node != start)
		{
			node = previousNodes.get(node);
			path.add(0, node);
		}

		return path;
	}
}
