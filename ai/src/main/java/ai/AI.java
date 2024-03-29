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
import common.Obstacle;
import common.Plane;
import common.Rectangle;
import common.Timer;
import common.Vectorf2;

public class AI
{
	private static List<Plane> escapingPlanes;

	private static Map<Plane, Float> fuelUsageRates;

	private static Map<Plane, Float> planeAges;

	private static Map<Plane, Float> previousFuel;

	static
	{
		escapingPlanes = new ArrayList<Plane>();
		fuelUsageRates = new HashMap<Plane, Float>();
		planeAges = new HashMap<Plane, Float>();
		previousFuel = new HashMap<Plane, Float>();
	}

	private static void calculateData()
	{
		Rectangle boundary = Data.getInstance().boundary;
		Data.getInstance().center =
				new Vectorf2((boundary.max.x - boundary.min.x) / 2.0f + boundary.min.x,
						(boundary.max.y - boundary.min.y) / 2.0f + boundary.min.y);

		for (Obstacle obstacle : Data.getInstance().obstacles)
		{
			obstacle.collisionRadius =
					Vectorf2.subtract(obstacle.boundary.max, obstacle.boundary.min).getMagnitude() / 2.0f;
			obstacle.repulsionRadius = obstacle.collisionRadius * 2.0f;
		}

		for (Plane plane : Data.getInstance().planes)
		{
			// Rotate 180 degrees to get out of the silly coordinate system where positive y (down) is 0 degrees.
			plane.rotation = (float) Math.toRadians(plane.rotation + 180.0f);
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

	public static float getAge(Plane plane)
	{
		return planeAges.get(plane);
	}

	public static Vectorf2 getEscapeVector(Plane plane)
	{
		Vectorf2 fromCenterToPosition = Vectorf2.subtract(plane.position, Data.getInstance().center);

		Vectorf2 escapeVector = fromCenterToPosition.copy();

		if (Math.abs(escapeVector.x) > Math.abs(escapeVector.y))
		{
			escapeVector.divide(Math.abs(escapeVector.x));
			float boundaryRangeX = Data.getInstance().boundary.max.x - Data.getInstance().boundary.min.x;
			escapeVector.multiply(boundaryRangeX / 2.0f);
		}
		else
		{
			escapeVector.divide(Math.abs(escapeVector.y));
			float boundaryRangeY = Data.getInstance().boundary.max.y - Data.getInstance().boundary.min.y;
			escapeVector.multiply(boundaryRangeY / 2.0f);
		}

		escapeVector.subtract(fromCenterToPosition);

		return escapeVector;
	}

	public static float getFuelUsageRate(Plane plane)
	{
		if (AIConfig.getInstance().fuelConsumptionRate != 0.0f)
		{
			return AIConfig.getInstance().fuelConsumptionRate;
		}

		if (fuelUsageRates.get(plane) != null && fuelUsageRates.get(plane) != 0.0f)
		{
			return fuelUsageRates.get(plane);
		}

		return 0.0f;
	}

	public static boolean isFuelLow(Plane plane)
	{
		if (getFuelUsageRate(plane) == 0.0f)
		{
			return false;
		}

		float escapeTime = getEscapeVector(plane).getMagnitude() / plane.speed * AIConfig.getInstance().escapeTime;

		if (plane.fuel / getFuelUsageRate(plane) <= escapeTime)
		{
			escapingPlanes.add(plane);
			return true;
		}

		return false;
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
				FlightPath.setInstance(JSON.fromFlightPathFile(AIConfig.getInstance().flightPathFilePath, 512));
				configDelta = 0.0f;
			}

			if (new File(AIConfig.getInstance().dataFilePath).exists())
			{
				Data.setInstance(JSON.fromDataFile(AIConfig.getInstance().dataFilePath, 2048));
				// If we couldn't manage to read the data file.
				if (Data.getInstance().boundary == null)
				{
					continue;
				}

				// Calculate some extra data based on what is given...
				calculateData();
			}

			Instructions.setInstance(JSON.fromInstructionUrl("http://192.168.21.206/ui/override.json", 1024));

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
		List<Node> flightPath = FlightPath.getInstance();
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
		PathFollower pathFollower = new PathFollower(path, plane.speed, AIConfig.getInstance().destinationThreshold);
		pathFollowers.put(plane, pathFollower);
	}

	public void advance(float deltaTime)
	{
		for (Plane plane : Data.getInstance().planes)
		{
			if (previousFuel.get(plane) != null)
			{
				fuelUsageRates.put(plane, (previousFuel.get(plane) - plane.fuel) / deltaTime);
			}
			previousFuel.put(plane, plane.fuel);
			if (planeAges.get(plane) == null)
			{
				planeAges.put(plane, 0.0f);
			}
			planeAges.put(plane, planeAges.get(plane) + deltaTime);

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
					if (plane.destination.equals(new Vectorf2(0.0f, 0.0f)))
					{
						escapingPlanes.add(plane);
					}
					break;
				}
			}

			if (plane.destination == null)
			{
				if (escapingPlanes.contains(plane) || (getAge(plane) < 1.0f && isFuelLow(plane)))
				{
					plane.destination = Vectorf2.add(plane.position, getEscapeVector(plane));
				}
				else
				{
					// The runway is the default destination but the path follower may override this.
					plane.destination = Data.getInstance().runway;
					pathFollowers.get(plane).follow(plane);
				}
			}

			new Steerer(plane).steer(deltaTime);

			System.out.println("plane " + plane.id + " pos: " + plane.position + " head: " + plane.heading + " way: " + plane.waypoints.get(0) + " dest:" + plane.destination);
		}
	}
}
