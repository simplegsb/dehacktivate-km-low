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

		// If we are turning anti-clockwise, just turn that way 10 degrees as opposed to 350 degrees clockwise.
		if (angleToDestination > Math.PI)
		{
			angleToDestination -= (2.0f * (float) Math.PI);
		}

		float turnAngle = 0.0f;

		// Turn as fast as possible until the plane gets to the destination bearing.
		if (angleToDestination >= 0.0f)
		{
			turnAngle = Math.min(angleToDestination, plane.turn_speed_radians * deltaTime);
		}
		else
		{
			turnAngle = Math.max(angleToDestination, plane.turn_speed_radians * deltaTime * -1.0f);
		}

		Vectorf2 toWaypoint = plane.heading.copy();
		toWaypoint.rotate(turnAngle);

		Vectorf2 repulsionEffect = getRepulsionEffect();
		// Multiplied by two so that it is more urgent.
		repulsionEffect.multiply(AIConfig.getInstance().repulsion_strength_factor);
		toWaypoint.add(repulsionEffect);

		toWaypoint.normalize();
		toWaypoint.multiply(plane.speed * deltaTime * AIConfig.getInstance().waypoint_lead_time);

		plane.waypoints.add(Vectorf2.add(plane.position, toWaypoint));
	}
}
