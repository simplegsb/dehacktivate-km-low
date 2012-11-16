package common;

import java.util.ArrayList;
import java.util.List;

public class Plane
{
	public float collisionRadius;
	
	public int currentWaypointIndex;

	public Vectorf2 heading;

	public Vectorf2 newHeading;

	public float newRotation;

	public Vectorf2 newWaypoint;

	public Vectorf2 position;

	public float repulsionRadius;

	public float rotation;

	public float speed;
	
	public float turnSpeed;

	public List<Vectorf2> waypoints = new ArrayList<Vectorf2>();
}
