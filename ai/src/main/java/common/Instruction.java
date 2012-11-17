package common;

import java.util.ArrayList;
import java.util.List;

public class Instruction
{
	public int plane_id;

	public List<Vectorf2> waypoints;

	public Instruction()
	{
		waypoints = new ArrayList<Vectorf2>();
	}

	public int getPlane_id()
	{
		return plane_id;
	}

	public List<Vectorf2> getWaypoints()
	{
		return waypoints;
	}

	public void setPlane_id(int plane_id)
	{
		this.plane_id = plane_id;
	}
}
