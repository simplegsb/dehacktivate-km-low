package simulator;

import common.Rectangle;
import common.Vectorf2;

public class SimulatorConfig
{
	private static SimulatorConfig instance;

	public static SimulatorConfig getInstance()
	{
		if (instance == null)
		{
			instance = new SimulatorConfig();
		}

		return instance;
	}

	public static void setInstance(SimulatorConfig instance)
	{
		SimulatorConfig.instance = instance;
	}

	public Rectangle boundary;

	public float collisionRadius;

	public int frameRateCap;

	public Vectorf2 runway;

	public float spawnFrequency;

	public float speed;

	public double turnSpeed;

	public float waypointReachedThreshold;

	public Rectangle getBoundary()
	{
		return boundary;
	}

	public float getCollision_radius()
	{
		return collisionRadius;
	}

	public int getFrame_rate_cap()
	{
		return frameRateCap;
	}

	public Vectorf2 getRunway()
	{
		return runway;
	}

	public float getSpawn_frequency()
	{
		return spawnFrequency;
	}

	public float getSpeed()
	{
		return speed;
	}

	public double getTurn_speed()
	{
		return turnSpeed;
	}

	public float getWaypoint_reached_threshold()
	{
		return waypointReachedThreshold;
	}

	public void setBoundary(Rectangle boundary)
	{
		this.boundary = boundary;
	}

	public void setCollision_radius(float collisionRadius)
	{
		this.collisionRadius = collisionRadius;
	}

	public void setFrame_rate_cap(int frame_rate_cap)
	{
		this.frameRateCap = frame_rate_cap;
	}

	public void setRunway(Vectorf2 runway)
	{
		this.runway = runway;
	}

	public void setSpawn_frequency(float spawn_frequency)
	{
		this.spawnFrequency = spawn_frequency;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setTurn_speed(double turnSpeed)
	{
		this.turnSpeed = turnSpeed;
	}

	public void setWaypoint_reached_threshold(float waypoint_reached_threshold)
	{
		this.waypointReachedThreshold = waypoint_reached_threshold;
	}
}
