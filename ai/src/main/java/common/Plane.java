package common;

import java.util.ArrayList;
import java.util.List;

public class Plane extends MileHighObject
{
	public int current_waypoint_index;

	public float fuel;

	public Vectorf2 heading;

	public int id;

	public String name;

	public Vectorf2 new_heading;

	public float new_rotation;

	public Vectorf2 new_waypoint;

	public int penalty;

	public int points;

	public float rotation;

	public float speed;

	public float turn_speed;

	public List<Vectorf2> waypoints;

	public Plane()
	{
		waypoints = new ArrayList<Vectorf2>();
	}

	public int getCurrent_waypoint_index()
	{
		return current_waypoint_index;
	}

	public float getFuel()
	{
		return fuel;
	}

	public Vectorf2 getHeading()
	{
		return heading;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@JSONIgnore
	public Vectorf2 getNew_heading()
	{
		return new_heading;
	}

	@JSONIgnore
	public float getNew_rotation()
	{
		return new_rotation;
	}

	@JSONIgnore
	public Vectorf2 getNew_waypoint()
	{
		return new_waypoint;
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
		return rotation;
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getTurn_speed()
	{
		return turn_speed;
	}

	public String getType()
	{
		return "plane";
	}

	public List<Vectorf2> getWaypoints()
	{
		return waypoints;
	}

	public void setCurrent_waypoint_index(int current_waypoint_index)
	{
		this.current_waypoint_index = current_waypoint_index;
	}

	public void setFuel(float fuel)
	{
		this.fuel = fuel;
	}

	public void setHeading(Vectorf2 heading)
	{
		this.heading = heading;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setNew_heading(Vectorf2 new_heading)
	{
		this.new_heading = new_heading;
	}

	public void setNew_rotation(float new_rotation)
	{
		this.new_rotation = new_rotation;
	}

	public void setNew_waypoint(Vectorf2 new_waypoint)
	{
		this.new_waypoint = new_waypoint;
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
		this.turn_speed = turn_speed;
	}

	public void setWaypoints(List<Vectorf2> waypoints)
	{
		this.waypoints = waypoints;
	}
}
