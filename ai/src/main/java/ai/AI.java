package ai;

import common.Data;
import common.Plane;
import common.Vectorf2;

public class AI
{
	public static void calculateNewHeadings(float deltaTime)
	{
		for (Plane plane : Data.getInstance().planes)
		{
			new SteeringAgent(plane).think(deltaTime);
		}
	}

	public static void calculateRepulsionRadii()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			plane.setRepulsion_radius(plane.collision_radius + plane.speed);
		}
	}

	public static void calculateWaypoints()
	{
		for (Plane plane : Data.getInstance().planes)
		{
			plane.new_waypoint = Vectorf2.add(plane.position, Vectorf2.multiply(plane.new_heading, plane.speed));
		}
	}

	public static void main(String[] args)
	{
		while (true)
		{
			float deltaTime = 0.0f; // TODO!

			readData();
			calculateRepulsionRadii();
			calculateNewHeadings(deltaTime);
			readWaypoints();
			calculateWaypoints();
			writeInstructions();
		}
	}

	public static void readData()
	{
		// TODO!
	}

	public static void readWaypoints()
	{
		// TODO!
	}

	public static void writeInstructions()
	{
		// TODO!
	}
}
