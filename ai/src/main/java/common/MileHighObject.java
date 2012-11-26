package common;

public abstract class MileHighObject
{
	public float collisionRadius;

	public Vectorf2 position;

	public float repulsionRadius;

	public float getCollision_radius()
	{
		return collisionRadius;
	}

	public Vectorf2 getPosition()
	{
		return position;
	}

	public abstract String getType();

	public void setCollision_radius(float collision_radius)
	{
		this.collisionRadius = collision_radius;
	}

	public void setPosition(Vectorf2 position)
	{
		this.position = position;
	}

	@JSONIgnore
	public void setType(String type)
	{
	}
}
