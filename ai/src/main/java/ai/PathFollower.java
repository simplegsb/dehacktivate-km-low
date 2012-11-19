package ai;

import java.util.List;

import common.Plane;
import common.Vectorf2;

public class PathFollower
{
	private Vectorf2 destination;

	private boolean endReached;

	private float gridSize;

	private int nextWaypointIndex;

	private List<Node> path;

	private Plane plane;

	private int previousWaypointIndex;

	public PathFollower(Plane plane, List<Node> path, float gridSize)
	{
		this.gridSize = gridSize;
		this.path = path;
		this.plane = plane;

		destination = null;
		endReached = false;
		previousWaypointIndex = -1;
		nextWaypointIndex = -1;
	}

	private void advanceDestinationOnPath(float distance)
	{
		if (previousWaypointIndex == -1)
		{
			previousWaypointIndex = 0;
			nextWaypointIndex = 1;
		}

		Vectorf2 advancement = Vectorf2.subtract(path.get(nextWaypointIndex).getPosition(),
				path.get(previousWaypointIndex).getPosition());
		advancement.normalize();
		advancement.multiply(distance);
		Vectorf2 destinationCopy = destination.copy();
		destinationCopy.add(advancement); 

		float distancePastNextWaypoint =
				Vectorf2.subtract(destinationCopy, path.get(previousWaypointIndex).getPosition()).getMagnitude() -
				Vectorf2.subtract(path.get(nextWaypointIndex).getPosition(),
						path.get(previousWaypointIndex).getPosition()).getMagnitude();

		if (distancePastNextWaypoint > 0.0f)
		{
			destination = path.get(nextWaypointIndex).getPosition();

			if (path.size() > nextWaypointIndex + 1)
			{
				previousWaypointIndex++;
				nextWaypointIndex++;

				advanceDestinationOnPath(distancePastNextWaypoint);
			}
		}
		else
		{
			destination.add(advancement);
		}
	}

	public void follow()
	{
		if (path.size() <= 1 || endReached)
		{
			destination = plane.destination;
		}
		else
		{
			if (Vectorf2.subtract(path.get(path.size() - 1).getPosition(),
					plane.position).getMagnitude() <= gridSize * 1.5f)
			{
				endReached = true;
			}

			if (previousWaypointIndex == -1)
			{
				destination = path.get(0).getPosition();
			}

			if (Vectorf2.subtract(destination, plane.position).getMagnitude() <= gridSize * 1.5f)
			{
				advanceDestinationOnPath(gridSize * 1.5f);
			}

			plane.destination = destination;
		}
	}
}
