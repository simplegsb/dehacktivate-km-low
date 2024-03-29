package common;

import java.util.ArrayList;
import java.util.List;

public class Plane extends MileHighObject
{
	public int currentWaypointIndex;

	public Vectorf2 destination;

	public float fuel;

	public Vectorf2 heading;

	public int id;

	public String name;

	public int penalty;

	public int points;

	public float rotation;

	public float speed;

	public float turnSpeed;

	public List<Vectorf2> waypoints;

	public Plane()
	{
		waypoints = new ArrayList<Vectorf2>();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object == this)
		{
			return true;
		}

		if (!(object instanceof Plane))
		{
			return false;
		}

		if (((Plane) object).id == id)
		{
			return true;
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + id;
		return hash;
	}

	public int getCurrent_waypoint_index()
	{
		return currentWaypointIndex;
	}

	public float getFuel()
	{
		return fuel;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getPenalty()
	{
		return penalty;
	}

	public int getPoints()
	{
		return points;
	}

	public float getRotation()
	{
		// Rotate to get INTO the silly coordinate system where positive y (down) is 0 degrees.
		// This is only used by the simulator when it creates data.json.
		return (float) Math.toDegrees(rotation + Math.PI);
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getTurn_speed()
	{
		// This is only used by the simulator when it creates data.json.
		return (float) Math.toDegrees(turnSpeed);
	}

	public String getType()
	{
		return "plane";
	}

	public List<Vectorf2> getWaypoints()
	{
		return waypoints;
	}

	public void setCrashing(boolean crashing)
	{
	}

	public void setCurrent_waypoint_index(int current_waypoint_index)
	{
		this.currentWaypointIndex = current_waypoint_index;
	}

	public void setFuel(float fuel)
	{
		this.fuel = fuel;
	}

	public void setGraphic(String graphic)
	{
	}

	public void setGraphic_full_path(String graphic_full_path)
	{
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setLanding(boolean landing)
	{
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPenalty(int penalty)
	{
		this.penalty = penalty;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public void setTurn_speed(float turn_speed)
	{
		this.turnSpeed = turn_speed;
	}

	public void setWaypoints(List<Vectorf2> waypoints)
	{
		this.waypoints = waypoints;
	}
}
