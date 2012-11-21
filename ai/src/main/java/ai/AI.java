package ai;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Data;
import common.Instruction;
import common.Instructions;
import common.JSON;
import common.Plane;
import common.Timer;
import common.Vectorf2;

public class AI
{
	private static List<Node> flightPath;

	private static Map<Plane, Float> fuelUsageRates;

	private static Map<Plane, Float> previousFuel;

	static
	{
		flightPath = new ArrayList<Node>();
		Node node0 = new Node();
		node0.setPosition(new Vectorf2(275.0f, 275.0f));
		flightPath.add(node0);
		Node node1 = new Node();
		node1.setPosition(new Vectorf2(275.0f, 550.0f));
		flightPath.add(node1);
		Node node2 = new Node();
		node2.setPosition(new Vectorf2(275.0f, 825.0f));
		flightPath.add(node2);
		Node node3 = new Node();
		node3.setPosition(new Vectorf2(550.0f, 825.0f));
		flightPath.add(node3);
		Node node4 = new Node();
		node4.setPosition(new Vectorf2(825.0f, 825.0f));
		flightPath.add(node4);
		Node node5 = new Node();
		node5.setPosition(new Vectorf2(825.0f, 550.0f));
		flightPath.add(node5);
		Node node6 = new Node();
		node6.setPosition(new Vectorf2(825.0f, 275.0f));
		flightPath.add(node6);
		Node node7 = new Node();
		node7.setPosition(new Vectorf2(550.0f, 275.0f));
		flightPath.add(node7);

		fuelUsageRates = new HashMap<Plane, Float>();
		previousFuel = new HashMap<Plane, Float>();
	}

	private static void calculateData()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			// Rotate 180 degrees to get out of the silly coordinate system where positive y (down) is 0 degrees.
			// Also adjust because our rotations are in the opposite direction.
			plane.rotation = (float) Math.toRadians(plane.rotation * -1.0f + 180.0f);
			plane.heading = new Vectorf2();
			plane.heading.toUnitRotation(plane.rotation);
			plane.turnSpeed = (float) Math.toRadians(plane.turnSpeed);
			plane.repulsionRadius = plane.collisionRadius + (plane.speed / plane.turnSpeed);

			// TODO This just aids in testing because the simulator sends waypoints we don't get from the real system...
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

			if (new File(AIConfig.getInstance().dataFilePath).exists())
			{
				Data.setInstance(JSON.fromDataFile(AIConfig.getInstance().dataFilePath, 2048));
				// Calculate some extra data based on what is given...
				calculateData();
			}

			if (new File(AIConfig.getInstance().overrideFilePath).exists())
			{
				Instructions.setInstance(JSON.fromInstructionFile(AIConfig.getInstance().overrideFilePath, 1024));
			}
			else
			{
				Instructions.getInstance().clear();
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

		JSON.toArrayFile(Instructions.getInstance(), AIConfig.getInstance().instructionsFilePath);
	}

	private Map<Plane, PathFollower> pathFollowers;

	public AI()
	{
		pathFollowers = new HashMap<Plane, PathFollower>();
	}

	public void addPathFollower(Plane plane)
	{
		Node closestNode = null;
		for (Node node : flightPath)
		{
			if (closestNode == null || Vectorf2.subtract(node.getPosition(), plane.position).getMagnitude() <
					Vectorf2.subtract(closestNode.getPosition(), plane.position).getMagnitude())
			{
				closestNode = node;
			}
		}

		List<Node> path = flightPath.subList(flightPath.indexOf(closestNode), flightPath.size());
		PathFollower pathFollower = new PathFollower(path, plane.speed, 100.0f);
		pathFollowers.put(plane, pathFollower);
	}

	public void advance(float deltaTime)
	{
		for (Plane plane : Data.getInstance().planes)
		{
			if (previousFuel.get(plane) != null)
			{
				fuelUsageRates.put(plane, (plane.fuel - previousFuel.get(plane)) * deltaTime);
			}
			previousFuel.put(plane, plane.fuel);

			if (pathFollowers.get(plane) == null)
			{
				addPathFollower(plane);
			}

			// Manual instructions take precedence.
			for (Instruction instruction : Instructions.getInstance())
			{
				if (instruction.planeId == plane.id)
				{
					plane.destination = instruction.waypoints.get(0);
					break;
				}
			}

			if (plane.destination == null)
			{
				/*if (isFuelLow(plane))
				{
					Vectorf2 awayFromRunway = Vectorf2.subtract(plane.position, Data.getInstance().runway);
					awayFromRunway.normalize();
					awayFromRunway.multiply(1000000.0f); // Times ONE MILLION! Get out of here!
					plane.destination = Vectorf2.add(plane.position, awayFromRunway);
				}
				else
				{*/
					plane.destination = Data.getInstance().runway;
					pathFollowers.get(plane).follow(plane);
				//}
			}

			new Steerer(plane).steer(deltaTime);
		}
	}

	private boolean isFuelLow(Plane plane)
	{
		if (fuelUsageRates.get(plane) == null)
		{
			return false;
		}

		return fuelUsageRates.get(plane) * plane.fuel <= AIConfig.getInstance().lowFuelThreshold;
	}
}
