package ai;

import common.Data;
import common.MileHighObject;
import common.Plane;
import common.Vectorf2;

public class SteeringAgent
{
	private Plane plane;

	public SteeringAgent(Plane plane)
	{
		this.plane = plane;
	}

	private Vectorf2 getRepulsionEffect()
	{
		Vectorf2 cumulativeRepulsionEffect = new Vectorf2();

		for (MileHighObject object : Data.getInstance().getObjects())
		{
			// We don't want the plane to repel itself...
			if (object == plane)
			{
				continue;
			}

			Vectorf2 repulsionEffect = Vectorf2.subtract(plane.position, object.position);
			float distanceBetweenCenters = repulsionEffect.getMagnitude();
			float distanceBetweenCollisionRadii =
					distanceBetweenCenters - (plane.collision_radius + object.collision_radius);
			float repulsionBuffer = distanceBetweenCenters - (plane.repulsion_radius + object.repulsion_radius);

			// If the plane is getting too close to the repulsion zone and is heading towards it.
			// Heading towards it is what the dot product tests.
			if (Vectorf2.dotProduct(plane.heading, repulsionEffect) < 0.0f &&
					distanceBetweenCollisionRadii < repulsionBuffer)
			{
				// The further into the repulsion zone the plane gets, the more it gets repelled.
				// The plane should be repelled with full force BEFORE it enters the collision radius.
				float repulsionFactor = (repulsionBuffer - distanceBetweenCollisionRadii) / repulsionBuffer;

				repulsionEffect.normalize();
				repulsionEffect.multiply(repulsionFactor);
				cumulativeRepulsionEffect.add(repulsionEffect);
			}
		}

		if (cumulativeRepulsionEffect.getMagnitude() > 1.0f)
		{
			cumulativeRepulsionEffect.normalize();
		}

		return cumulativeRepulsionEffect;
	}

	public void think(float deltaTime)
	{
		Vectorf2 toDestination = Vectorf2.subtract(plane.destination, plane.position);
		float angleToDestination = plane.heading.angleTo(toDestination);

		// Turn as fast as possible until the plane gets to the destination bearing.
		float turnAngle = Math.min(angleToDestination, plane.turn_speed * deltaTime);

		Vectorf2 newHeading = plane.heading.copy();
		newHeading.rotate(turnAngle);

		Vectorf2 repulsionEffect = getRepulsionEffect();
		// Multiplied by two so that it is more urgent.
		repulsionEffect.multiply(AIConfig.getInstance().repulsion_strength_factor);
		newHeading.add(repulsionEffect);

		// We need the waypoint to lead the plane by enough so that it isn't constantly being reached.
		// Every time we reach a waypoint we increase the size of the waypoint array we need to re-create
		// which in turn increases the file size etc.
		newHeading.normalize();
		newHeading.multiply(plane.speed * AIConfig.getInstance().steering_waypoint_lead_factor);

		plane.waypoints.add(plane.current_waypoint_index, Vectorf2.add(plane.position, newHeading));
	}
}
