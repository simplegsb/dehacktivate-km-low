package ai;

import common.Data;
import common.Obstacle;
import common.Plane;
import common.Vectorf2;

public class SteeringAgent
{
	private Vectorf2 destination;

	private Plane plane;

	public SteeringAgent(Plane plane)
	{
		this.plane = plane;
	}

	public Vectorf2 getDestination()
	{
		return destination;
	}

	private Vectorf2 getRepulsionEffect()
	{
		Vectorf2 cumulativeRepulsionEffect = new Vectorf2();

		for (Obstacle obstacle : Data.getInstance().obstacles)
		{
			Vectorf2 repulsionEffect = Vectorf2.subtract(plane.position, obstacle.position);
			float distanceBetween = repulsionEffect.getMagnitude();
			float repulsionRadiusSum = plane.repulsion_radius + obstacle.repulsion_radius;

			if (Vectorf2.dotProduct(plane.heading, repulsionEffect) < 0.0f &&
					distanceBetween < repulsionRadiusSum)
			{
				float repulsionFactor = (repulsionRadiusSum - distanceBetween) / repulsionRadiusSum;
				repulsionEffect.normalize();
				repulsionEffect.multiply(repulsionFactor);
				cumulativeRepulsionEffect.add(repulsionEffect);
			}
		}

		for (Plane plane : Data.getInstance().planes)
		{
			// We don't want the plane to repel itself...
			if (plane == this.plane)
			{
				continue;
			}
			Vectorf2 repulsionEffect = Vectorf2.subtract(this.plane.position, plane.position);
			float distanceBetween = repulsionEffect.getMagnitude();
			float repulsionRadiusSum = this.plane.repulsion_radius + plane.repulsion_radius;

			if (Vectorf2.dotProduct(this.plane.heading, repulsionEffect) < 0.0f &&
					distanceBetween < repulsionRadiusSum)
			{
				float repulsionFactor = (repulsionRadiusSum - distanceBetween) / repulsionRadiusSum;
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

	public void setDestination(Vectorf2 destination)
	{
		this.destination = destination;
	}

	public void think(float deltaTime)
	{
		Vectorf2 toDestination = Vectorf2.subtract(destination, plane.position);
		float angleToDestination = plane.heading.angleTo(toDestination);
		float turnAngle = Math.min(angleToDestination, plane.turn_speed);

		Vectorf2 newHeading = new Vectorf2();
		newHeading.toUnitRotation(turnAngle);

		Vectorf2 repulsionEffect = getRepulsionEffect();
		repulsionEffect.multiply(2.0f); // Multiplied by two so that it is more urgent.
		newHeading.add(repulsionEffect);

		newHeading.normalize();

		plane.new_heading = newHeading;
		plane.new_rotation = newHeading.getRotation();
	}
}
