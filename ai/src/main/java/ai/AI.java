package ai;

import java.io.File;

import common.Data;
import common.Instruction;
import common.Instructions;
import common.JSON;
import common.Plane;
import common.Timer;
import common.Vectorf2;

public class AI
{
	public static void calculateData()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			plane.destination = Data.getInstance().runway;
			plane.heading = new Vectorf2();
			plane.heading.toUnitRotation(plane.rotation);
			plane.repulsion_radius = plane.collision_radius + plane.speed;

			// Re-add reached waypoints so we can tag the new one on the end.
			while (plane.current_waypoint_index > plane.waypoints.size())
			{
				plane.waypoints.add(new Vectorf2());
			}
		}
	}

	public static void main(String[] args)
	{
		Timer timer = new Timer();
		timer.start();
		float configDelta = 1.0f;

		while (true)
		{
			timer.tick();
			configDelta += timer.getDeltaTime();

			// Re-read the config file once a second for tweaking on the fly.
			if (configDelta >= 1.0f)
			{
				AIConfig.setInstance(JSON.fromFile(AIConfig.class, "ai-config.json", 256));
				configDelta = 0.0f;
			}

			Data.setInstance(JSON.fromFile(Data.class, "data.json", 2048));
			// Calculate some extra data based on what is given...
			calculateData();

			if (new File("waypoints.json").exists())
			{
				//Waypoints.setInstance(JSON.fromFileToObject(Waypoints.class, "waypoints.json", 1024));
				//readWaypoints();
			}

			// TODO Path finding for planes that didn't have manual waypoints.
			// Maybe only once per second on separate thread?
			// When a path is found, the next waypoint on that path should be set to the plane.destination.
			// That is what the steering agent tries to steer to.

			for (Plane plane : Data.getInstance().planes)
			{
				new SteeringAgent(plane).think(timer.getDeltaTime());
			}

			writeInstructions();

			if (AIConfig.getInstance().frame_rate_cap != 0)
			{
				timer.waitUntilDeltaReaches(1.0f / AIConfig.getInstance().frame_rate_cap);
			}
		}
	}

	public static void writeInstructions()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			Instruction instruction = new Instruction();
			instruction.plane_id = plane.id;
			instruction.waypoints = plane.waypoints;

			Instructions.getInstance().add(instruction);
		}

		JSON.toFile(Instructions.getInstance(), "instructions.json");
	}
}
