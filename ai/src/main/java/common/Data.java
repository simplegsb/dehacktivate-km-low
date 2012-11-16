package common;

import java.util.ArrayList;
import java.util.List;

public class Data
{
	private static Data instance;

	public static Data getInstance()
	{
		if (instance == null)
		{
			instance = new Data();
		}

		return instance;
	}

	public static void setInstance(Data instance)
	{
		Data.instance = instance;
	}

	public Rectangle boundary;

	public List<Obstacle> obstacles;

	public List<Plane> planes;

	public Vectorf2 runway;

	public Data()
	{
		obstacles = new ArrayList<Obstacle>();
		planes = new ArrayList<Plane>();
	}

	public Rectangle getBoundary() {
		return boundary;
	}

	public List<Object> getObjects()
	{
		List<Object> objects = new ArrayList<Object>();
		objects.addAll(obstacles);
		objects.addAll(planes);
		
		return objects;
	}

	public Vectorf2 getRunway() {
		return runway;
	}

	public void setBoundary(Rectangle boundary) {
		this.boundary = boundary;
	}

	public void setRunway(Vectorf2 runway) {
		this.runway = runway;
	}
}
