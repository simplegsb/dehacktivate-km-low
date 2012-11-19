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

	public float getRepulsion_radius()
	{
		return repulsionRadius;
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

	public void setRepulsion_radius(float repulsion_radius)
	{
		this.repulsionRadius = repulsion_radius;
	}

	@JSONIgnore
	public void setType(String type)
	{
	}
}
