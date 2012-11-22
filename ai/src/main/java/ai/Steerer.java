package ai;

import java.util.List;

import common.Data;
import common.MileHighObject;
import common.Obstacle;
import common.Plane;
import common.Vectorf2;

public class Steerer
{
	private Plane plane;

	public Steerer(Plane plane)
	{
		this.plane = plane;
	}

	private Vectorf2 getRepulsionEffect()
	{
		Vectorf2 cumulativeRepulsionEffect = new Vectorf2();
		List<MileHighObject> objects = Data.getInstance().getObjects();

		if (!AI.isFuelLow(plane))
		{
			// Repel the edges of the map too, lets not give away points we don't need to!
			// Although... better to lose a plane than have a crash so lets make these less influential.
			Obstacle topBoundary = new Obstacle();
			topBoundary.position = new Vectorf2(plane.position.x, Data.getInstance().boundary.min.y);
			topBoundary.collisionRadius = plane.speed / plane.turnSpeed * AIConfig.getInstance().boundaryRepelFactor;
			objects.add(topBoundary);
			Obstacle leftBoundary = new Obstacle();
			leftBoundary.position = new Vectorf2(Data.getInstance().boundary.min.x, plane.position.y);
			leftBoundary.collisionRadius = plane.speed / plane.turnSpeed * AIConfig.getInstance().boundaryRepelFactor;
			objects.add(leftBoundary);
			Obstacle bottomBoundary = new Obstacle();
			bottomBoundary.position = new Vectorf2(plane.position.x, Data.getInstance().boundary.max.y);
			bottomBoundary.collisionRadius = plane.speed / plane.turnSpeed * AIConfig.getInstance().boundaryRepelFactor;
			objects.add(bottomBoundary);
			Obstacle rightBoundary = new Obstacle();
			rightBoundary.position = new Vectorf2(Data.getInstance().boundary.max.x, plane.position.y);
			rightBoundary.collisionRadius = plane.speed / plane.turnSpeed * AIConfig.getInstance().boundaryRepelFactor;
			objects.add(rightBoundary);
		}

		for (MileHighObject object : objects)
		{
			// We don't want the plane to repel itself...
			if (object == plane)
			{
				continue;
			}

			Vectorf2 repulsionEffect = Vectorf2.subtract(plane.position, object.position);
			float distanceBetweenCenters = repulsionEffect.getMagnitude();
			float distanceBetweenCollisionRadii =
					distanceBetweenCenters - (plane.collisionRadius + object.collisionRadius);
			float repulsionBuffer = plane.repulsionRadius + object.repulsionRadius;

			if (object instanceof Obstacle)
			{
				repulsionBuffer *= AIConfig.getInstance().obstacleRepelFactor;
			}
			else if (object instanceof Plane)
			{
				repulsionBuffer *= AIConfig.getInstance().planeRepelFactor;
			}

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

	public void steer(float deltaTime)
	{
		Vectorf2 toDestination = Vectorf2.subtract(plane.destination, plane.position);
		float angleToDestination = plane.heading.angleTo(toDestination);

		// If we are turning 350 degrees one way, just turn 10 degrees the other way instead.
		if (angleToDestination > Math.PI)
		{
			angleToDestination -= (2.0f * (float) Math.PI);
		}

		float turnAngle = 0.0f;

		// Turn as fast as possible until the plane gets to the destination bearing.
		if (angleToDestination >= 0.0f)
		{
			turnAngle = Math.min(angleToDestination, plane.turnSpeed * deltaTime);
		}
		else
		{
			turnAngle = Math.max(angleToDestination, plane.turnSpeed * deltaTime * -1.0f);
		}

		Vectorf2 toWaypoint = plane.heading.copy();
		toWaypoint.rotate(turnAngle);

		Vectorf2 repulsionEffect = getRepulsionEffect();
		repulsionEffect.multiply(AIConfig.getInstance().repulsionStrengthFactor);
		toWaypoint.add(repulsionEffect);

		toWaypoint.normalize();
		toWaypoint.multiply(plane.speed * AIConfig.getInstance().waypointLeadTime);

		plane.waypoints.add(Vectorf2.add(plane.position, toWaypoint));
	}
}
