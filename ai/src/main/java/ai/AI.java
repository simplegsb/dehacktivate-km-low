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
	private static void calculateData()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			// Rotate 180 degrees to get out of the silly coordinate system where positive y (down) is 0 degrees.
			// Also adjust because our rotations are in the opposite direction.
			plane.rotation = (float) Math.toRadians(plane.rotation * -1.0f + 180.0f);
			plane.heading = new Vectorf2();
			plane.heading.toUnitRotation(plane.rotation);
			plane.repulsionRadius = plane.collisionRadius + plane.speed;
			plane.turnSpeed = (float) Math.toRadians(plane.turnSpeed);

			// TODO This just aids in testing because the simulator sends waypoints...
			plane.waypoints.clear();

			// Re-add reached waypoints so we can tag the new one on the end.
			while (plane.currentWaypointIndex > plane.waypoints.size())
			{
				plane.waypoints.add(new Vectorf2());
			}
		}
	}

	public static void main(String[] args)
	{
		AI ai = new AI();
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
				AIConfig.setInstance(JSON.fromObjectFile(AIConfig.class, "ai-config.json", 256));
				configDelta = 0.0f;
			}

			if (new File("data.json").exists())
			{
				Data.setInstance(JSON.fromDataFile("data.json", 2048));
				// Calculate some extra data based on what is given...
				calculateData();
			}

			if (new File("manual-instructions.json").exists())
			{
				Instructions.getInstance().clear();
				Instructions.setInstance(JSON.fromInstructionFile("manual-instructions.json", 1024));
			}

			ai.advance(timer.getDeltaTime());

			Instructions.getInstance().clear();
			writeInstructions();

			if (AIConfig.getInstance().frameRateCap != 0)
			{
				timer.waitUntilDeltaReaches(1.0f / AIConfig.getInstance().frameRateCap);
			}
		}
	}

	private static void writeInstructions()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			Instruction instruction = new Instruction();
			instruction.planeId = plane.id;
			instruction.waypoints = plane.waypoints;

			Instructions.getInstance().add(instruction);
		}

		JSON.toArrayFile(Instructions.getInstance(), "instructions.json");
	}

	public void advance(float deltaTime)
	{
		for (Plane plane : Data.getInstance().planes)
		{
			// Manual instructions take precedence.
			/*for (Instruction instruction : Instructions.getInstance())
			{
				if (instruction.planeId == plane.id)
				{
					plane.destination = instruction.waypoints.get(0);
					break;
				}
			}*/

			//if (plane.destination == null)
			{
				plane.destination = Data.getInstance().runway;
			}

			new Steerer(plane).steer(deltaTime);
		}
	}
}
