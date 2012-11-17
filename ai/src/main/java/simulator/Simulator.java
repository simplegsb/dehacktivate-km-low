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
				System.out.println("\tInstruction for plane " + instruction.plane_id + " contains waypoints:");
				for (Vectorf2 waypoint : instruction.waypoints)
				{
					System.out.println("\t\t" + waypoint);
				}
			}
			// End Debugging.

			simulator.advance(timer.getDeltaTime());

			JSON.toObjectFile(Data.getInstance(), "data.json");

			if (SimulatorConfig.getInstance().frame_rate_cap != 0)
			{
				timer.waitUntilDeltaReaches(1.0f / SimulatorConfig.getInstance().frame_rate_cap);
			}
		}
	}

	public static void updateWaypoints()
	{
		for (Instruction instruction : Instructions.getInstance())
		{
			Plane plane = null;
			for (Plane currentPlane : Data.getInstance().planes)
			{
				if (currentPlane.id == instruction.plane_id)
				{
					plane = currentPlane;
				}
			}

			if (plane == null)
			{
				continue;
			}

			// Ignore waypoints before the current one.
			for (int index = plane.current_waypoint_index; index < instruction.waypoints.size(); index++)
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
		}
	}

	public int planeId;

	public int score;

	public float spawnDelta;

	public Simulator()
	{
		planeId = 0;
		score = 0;
		spawnDelta = SimulatorConfig.getInstance().spawn_frequency;
	}

	public void advance(float deltaTime)
	{
		spawnDelta += deltaTime;
		if (spawnDelta >= SimulatorConfig.getInstance().spawn_frequency)
		{
			spawnPlane();
			spawnDelta = 0.0f;
		}

		Iterator<Plane> iterator = Data.getInstance().planes.iterator();
		while (iterator.hasNext())
		{
			Plane plane = iterator.next();

			float angleToWaypoint = 0.0f;
			if (plane.current_waypoint_index < plane.waypoints.size())
			{
				angleToWaypoint = getAngleToNextWaypoint(plane);
			}

			float turnAngle = 0.0f;

			// Turn as fast as possible until the plane gets to the waypoint bearing.
			if (angleToWaypoint >= 0.0f)
			{
				turnAngle = Math.min(angleToWaypoint, plane.turn_speed * deltaTime);
			}
			else
			{
				turnAngle = Math.max(angleToWaypoint, plane.turn_speed * deltaTime);
			}

			plane.heading.rotate(turnAngle);
			plane.rotation += turnAngle;

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
					SimulatorConfig.getInstance().waypoint_reached_threshold &&
					Math.abs(plane.position.y - SimulatorConfig.getInstance().runway.y) <
					SimulatorConfig.getInstance().waypoint_reached_threshold)
			{
				iterator.remove();
				System.out.println("Plane " + plane.id + " has landed!");
			}
		}
	}

	public float getAngleToNextWaypoint(Plane plane)
	{
		Vectorf2 toWaypoint =
				Vectorf2.subtract(plane.waypoints.get(plane.current_waypoint_index), plane.position);
		float angleToWaypoint = plane.heading.angleTo(toWaypoint);

		// If we are turning anti-clockwise, just turn that way 10 degrees as opposed to 350 degrees clockwise.
		if (angleToWaypoint > Math.PI)
		{
			angleToWaypoint = angleToWaypoint - (2.0f * (float) Math.PI);
		}

		// Has the plane reached the waypoint?
		if (toWaypoint.getMagnitude() <= SimulatorConfig.getInstance().waypoint_reached_threshold)
		{
			plane.current_waypoint_index++;
		}

		return angleToWaypoint;
	}

	public void spawnPlane()
	{
		Plane plane = new Plane();
		
		plane.collision_radius = 10.0f;
		plane.current_waypoint_index = 0;
		plane.heading = new Vectorf2();
		plane.id = planeId++;
		plane.position = new Vectorf2();
		plane.speed = 100.0f;
		plane.turn_speed = 10.0f;

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
