package simulator;

import common.Blackboard;
import common.Plane;
import common.Vectorf2;

public class Simulator
{
	public static void main(String[] args)
	{
		Simulator simulator = new Simulator(20.0f); // TODO data driven!

		while (true)
		{
			float deltaTime = 0.0f; // TODO!

			readInstructions();
			simulator.advance(deltaTime);
			writeState();
		}
	}

	public static void readInstructions()
	{
		// TODO!
	}

	public static void writeState()
	{
		// TODO!
	}

	public float spawnDelta;

	public float spawnFrequency;

	public Simulator(float spawnFrequency)
	{
		this.spawnFrequency = spawnFrequency;

		spawnDelta = spawnFrequency;
	}

	public void advance(float deltaTime)
	{
		spawnDelta += deltaTime;
		if (spawnDelta >= spawnFrequency)
		{
			spawnPlane();
			spawnDelta = 0.0f;
		}

		for (Plane plane : Blackboard.getInstance().getPlanes())
		{
			Vectorf2 toWaypoint = Vectorf2.subtract(plane.waypoints.get(plane.currentWaypointIndex), plane.position);
			float angleToWaypoint = plane.heading.angleTo(toWaypoint);
			if (angleToWaypoint > Math.PI)
			{
				angleToWaypoint = angleToWaypoint - (2.0f * (float) Math.PI);
			}

			float turnAngle = 0.0f;
			if (angleToWaypoint >= 0.0f)
			{
				turnAngle = Math.min(angleToWaypoint, plane.turnSpeed * deltaTime);
			}
			else
			{
				turnAngle = Math.max(angleToWaypoint, plane.turnSpeed * deltaTime);
			}

			plane.heading.rotate(turnAngle);
			plane.rotation += turnAngle;
			
			Vectorf2 movement = plane.heading.copy();
			movement.multiply(plane.speed * deltaTime);
			plane.position.add(movement);
		}
	}

	public void spawnPlane()
	{
		// TODO!
	}
}
