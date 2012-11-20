package simulator;

import java.io.File;
import java.util.Iterator;

import common.Data;
import common.Instruction;
import common.Instructions;
import common.JSON;
import common.Plane;
import common.Timer;
import common.Vectorf2;

public class Simulator
{
	public static void main(String[] args)
	{
		SimulatorConfig.setInstance(JSON.fromObjectFile(SimulatorConfig.class, "simulator-config.json", 128));
		Data.getInstance().boundary = SimulatorConfig.getInstance().boundary;
		Data.getInstance().runway = SimulatorConfig.getInstance().runway;

		Simulator simulator = new Simulator();
		Timer timer = new Timer();
		timer.start();

		while (true)
		{
			timer.tick();

			// Debugging.
			System.out.println("Planes:");
			for (Plane plane : Data.getInstance().planes)
			{
				System.out.println("\tPlane " + plane.id + " is at " + plane.position);
			}
			// End Debugging.

			File instructionsFile = new File("instructions.json");
			if (instructionsFile.exists())
			{
				Instructions.setInstance(JSON.fromInstructionFile("instructions.json", 1024));
				//instructionsFile.delete();
			}
			updateWaypoints();

			// Debugging.
			System.out.println("Instructions:");
			for (Instruction instruction : Instructions.getInstance())
			{
				System.out.println("\tInstruction for plane " + instruction.planeId + " contains waypoints:");
				for (Vectorf2 waypoint : instruction.waypoints)
				{
					System.out.println("\t\t" + waypoint);
				}
			}
			// End Debugging.

			simulator.advance(timer.getDeltaTime());

			JSON.toObjectFile(Data.getInstance(), "data.json");

			if (SimulatorConfig.getInstance().frameRateCap != 0)
			{
				timer.waitUntilDeltaReaches(1.0f / SimulatorConfig.getInstance().frameRateCap);
			}
		}
	}

	private static void updateWaypoints()
	{
		for (Instruction instruction : Instructions.getInstance())
		{
			for (Plane plane : Data.getInstance().planes)
			{
				if (plane.id == instruction.planeId)
				{
					// Ignore waypoints before the current one.
					for (int index = plane.currentWaypointIndex; index < instruction.waypoints.size(); index++)
					{
						if (index < plane.waypoints.size())
						{
							// Replace existing waypoints.
							plane.waypoints.set(index, instruction.waypoints.get(index));
						}
						else
						{
							// Append new waypoints.
							plane.waypoints.add(instruction.waypoints.get(index));
						}
					}

					break;
				}
			}
		}
	}

	public int planeId;

	public int score;

	public float spawnDelta;

	public Simulator()
	{
		planeId = 0;
		score = 0;
		spawnDelta = SimulatorConfig.getInstance().spawnFrequency;
	}

	public void advance(float deltaTime)
	{
		spawnDelta += deltaTime;
		if (spawnDelta >= SimulatorConfig.getInstance().spawnFrequency)
		{
			spawnPlane();
			spawnDelta = 0.0f;
		}

		Iterator<Plane> iterator = Data.getInstance().planes.iterator();
		while (iterator.hasNext())
		{
			Plane plane = iterator.next();

			float angleToWaypoint = 0.0f;
			if (plane.currentWaypointIndex < plane.waypoints.size())
			{
				angleToWaypoint = getAngleToNextWaypoint(plane);
			}

			float turnAngle = 0.0f;

			// Turn as fast as possible until the plane gets to the waypoint bearing.
			if (angleToWaypoint >= 0.0f)
			{
				turnAngle = Math.min(angleToWaypoint, plane.turnSpeed * deltaTime);
			}
			else
			{
				turnAngle = Math.max(angleToWaypoint, plane.turnSpeed * deltaTime * -1.0f);
			}

			plane.heading.rotate(turnAngle);
			plane.rotation = plane.heading.getRotation();

			plane.position.add(Vectorf2.multiply(plane.heading, plane.speed * deltaTime));

			if (plane.position.x < SimulatorConfig.getInstance().boundary.min.x ||
					plane.position.x > SimulatorConfig.getInstance().boundary.max.x ||
					plane.position.y < SimulatorConfig.getInstance().boundary.min.y ||
					plane.position.y > SimulatorConfig.getInstance().boundary.max.y)
			{
				iterator.remove();
				System.out.println("Plane " + plane.id + " has left the building (" + plane.position + ").");
			}

			if (Math.abs(plane.position.x - SimulatorConfig.getInstance().runway.x) <
					SimulatorConfig.getInstance().waypointReachedThreshold &&
					Math.abs(plane.position.y - SimulatorConfig.getInstance().runway.y) <
					SimulatorConfig.getInstance().waypointReachedThreshold)
			{
				iterator.remove();
				System.out.println("Plane " + plane.id + " has landed!");
			}
		}
	}

	private float getAngleToNextWaypoint(Plane plane)
	{
		Vectorf2 toWaypoint =
				Vectorf2.subtract(plane.waypoints.get(plane.currentWaypointIndex), plane.position);
		float angleToWaypoint = plane.heading.angleTo(toWaypoint);

		// If we are turning 350 degrees one way, just turn 10 degrees the other way instead.
		if (angleToWaypoint > Math.PI)
		{
			angleToWaypoint -= (2.0f * (float) Math.PI);
		}

		// Has the plane reached the waypoint?
		if (toWaypoint.getMagnitude() <= SimulatorConfig.getInstance().waypointReachedThreshold)
		{
			plane.currentWaypointIndex++;
		}

		return angleToWaypoint;
	}

	private void spawnPlane()
	{
		Plane plane = new Plane();

		plane.collisionRadius = 20.0f;
		plane.currentWaypointIndex = 0;
		plane.heading = new Vectorf2();
		plane.id = planeId++;
		plane.position = new Vectorf2();
		plane.speed = 10.0f;
		plane.turnSpeed = (float) Math.PI;

		if (Math.random() >= 0.5)
		{
			if (Math.random() >= 0.5)
			{
				plane.heading.x = -1.0f;
				plane.position.x = SimulatorConfig.getInstance().boundary.max.x;
			}
			else
			{
				plane.heading.x = 1.0f;
				plane.position.x = SimulatorConfig.getInstance().boundary.min.x;
			}

			float yRange = SimulatorConfig.getInstance().boundary.max.y - SimulatorConfig.getInstance().boundary.min.y;
			plane.position.y = (float) (SimulatorConfig.getInstance().boundary.min.y + Math.random() * yRange);
		}
		else
		{
			float xRange = SimulatorConfig.getInstance().boundary.max.x - SimulatorConfig.getInstance().boundary.min.x;
			plane.position.x = (float) (SimulatorConfig.getInstance().boundary.min.x + Math.random() * xRange);

			if (Math.random() >= 0.5)
			{
				plane.heading.y = -1.0f;
				plane.position.y = SimulatorConfig.getInstance().boundary.max.y;
			}
			else
			{
				plane.heading.y = 1.0f;
				plane.position.y = SimulatorConfig.getInstance().boundary.min.y;
			}
		}

		plane.rotation = plane.heading.getRotation();

		Data.getInstance().planes.add(plane);
	}
}
