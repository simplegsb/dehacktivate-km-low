package ai;

import java.util.List;

import common.Plane;
import common.Vectorf2;

public class PathFollower
{
	private Vectorf2 destination;

	private float destinationThreshold;

	private boolean endReached;

	private float advanceDistance;

	private int nextWaypointIndex;

	private List<Node> path;

	private int previousWaypointIndex;

	public PathFollower(List<Node> path, float advanceDistance, float destinationThreshold)
	{
		this.advanceDistance = advanceDistance;
		this.destinationThreshold = destinationThreshold;
		this.path = path;

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
		Vectorf2 newDestination = Vectorf2.add(destination, advancement);

		float distancePastNextWaypoint =
				Vectorf2.subtract(newDestination, path.get(previousWaypointIndex).getPosition()).getMagnitude() -
				Vectorf2.subtract(path.get(nextWaypointIndex).getPosition(),
						path.get(previousWaypointIndex).getPosition()).getMagnitude();

		if (distancePastNextWaypoint > 0.0f)
		{
			destination = path.get(nextWaypointIndex).getPosition().copy();

			if (path.size() > nextWaypointIndex + 1)
			{
				previousWaypointIndex++;
				nextWaypointIndex++;

				advanceDestinationOnPath(distancePastNextWaypoint);
			}
		}
		else
		{
			destination = newDestination;
		}
	}

	public void follow(Plane plane)
	{
		if (path.isEmpty() || endReached)
		{
			return;
		}

		if (Vectorf2.subtract(path.get(path.size() - 1).getPosition(),
				plane.position).getMagnitude() <= destinationThreshold)
		{
			endReached = true;
			return;
		}

		if (previousWaypointIndex == -1)
		{
			destination = path.get(0).getPosition().copy();
		}

		if (Vectorf2.subtract(destination, plane.position).getMagnitude() <= destinationThreshold)
		{
			advanceDestinationOnPath(advanceDistance);
		}

		plane.destination = destination;
	}
}
