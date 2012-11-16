package ai;

import common.Plane;
import common.Vectorf2;

public class AI
{
	public static void calculateNewHeadings(float deltaTime)
	{
		for (Plane plane : Blackboard.getInstance().getPlanes())
		{
			new SteeringAgent(plane).think(deltaTime);
		}
	}

	public static void calculateRepulsionRadii()
	{
		for (Plane plane : Blackboard.getInstance().getPlanes())
		{
			plane.repulsionRadius = plane.collisionRadius + plane.speed;
		}
	}

	public static void calculateWaypoints()
	{
		for (Plane plane : Blackboard.getInstance().getPlanes())
		{
			plane.newWaypoint = Vectorf2.add(plane.position, Vectorf2.multiply(plane.newHeading, plane.speed));
		}
	}

	public static void main(String[] args)
	{
		while (true)
		{
			float deltaTime = 0.0f; // TODO!

			readState();
			calculateRepulsionRadii();
			calculateNewHeadings(deltaTime);
			readWaypoints();
			calculateWaypoints();
			writeWaypoints();
		}
	}

	public static void readState()
	{
		// TODO!
	}

	public static void readWaypoints()
	{
		// TODO!
	}

	public static void writeWaypoints()
	{
		// TODO!
	}
}
