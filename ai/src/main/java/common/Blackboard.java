package common;

import java.util.List;


public class Blackboard
{
	private static Blackboard blackboard;

	public static Blackboard getInstance()
	{
		if (blackboard == null)
		{
			blackboard = new Blackboard();
		}

		return blackboard;
	}

	public List<Obstacle> obstacles;

	public List<Plane> planes;

	public List<Obstacle> getObstacles()
	{
		return obstacles;
	}

	public List<Plane> getPlanes()
	{
		return planes;
	}
}
