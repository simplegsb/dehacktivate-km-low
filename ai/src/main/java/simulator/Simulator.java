package simulator;

import common.Data;
import common.Instructions;
import common.JSON;
import common.Plane;
import common.Vectorf2;

public class Simulator
{
	public static void main(String[] args)
	{
		SimulatorConfig.setInstance(JSON.fromFileToObject(SimulatorConfig.class, "simulator-config.json", 128));
		Data.getInstance().boundary = SimulatorConfig.getInstance().boundary;
		Data.getInstance().runway = SimulatorConfig.getInstance().runway;

		Simulator simulator = new Simulator();

		while (true)
		{
			float deltaTime = 0.0f; // TODO!

			Instructions.setInstance(JSON.fromFileToObject(Instructions.class, "instructions.json", 1024));
			updateWaypoints();
			simulator.advance(deltaTime);
			JSON.fromObjectToFile(Data.getInstance(), "data.json");
		}
	}

	public static void updateWaypoints()
	{
		// TODO!
	}

	public int planeId;

	public float spawnDelta;

	public Simulator()
	{
		planeId = 0;
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

		for (Plane plane : Data.getInstance().planes)
		{
			float angleToWaypoint = 0.0f;
			if (!plane.waypoints.isEmpty())
			{
				Vectorf2 toWaypoint =
						Vectorf2.subtract(plane.waypoints.get(plane.current_waypoint_index), plane.position);
				angleToWaypoint = plane.heading.angleTo(toWaypoint);
				if (angleToWaypoint > Math.PI)
				{
					angleToWaypoint = angleToWaypoint - (2.0f * (float) Math.PI);
				}
			}

			float turnAngle = 0.0f;
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

			Vectorf2 movement = plane.heading.copy();
			movement.multiply(plane.speed * deltaTime);
			plane.position.add(movement);
		}
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

		if (Math.random() % 2 == 0)
		{
			if (Math.random() % 2 == 0)
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
			plane.position.y = (float) Math.random() % yRange;
		}
		else
		{
			float xRange = SimulatorConfig.getInstance().boundary.max.x - SimulatorConfig.getInstance().boundary.min.x;
			plane.position.x = (float) Math.random() % xRange;

			if (Math.random() % 2 == 0)
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
