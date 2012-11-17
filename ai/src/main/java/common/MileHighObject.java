package common;

public abstract class MileHighObject
{
	public float collision_radius;

	public Vectorf2 position;

	public float repulsion_radius;

	public float getCollision_radius()
	{
		return collision_radius;
	}

	public Vectorf2 getPosition()
	{
		return position;
	}

	public float getRepulsion_radius()
	{
		return repulsion_radius;
	}

	public abstract String getType();

	public void setCollision_radius(float collision_radius)
	{
		this.collision_radius = collision_radius;
	}

	public void setPosition(Vectorf2 position)
	{
		this.position = position;
	}

	public void setRepulsion_radius(float repulsion_radius)
	{
		this.repulsion_radius = repulsion_radius;
	}

	@JSONIgnore
	public void setType(String type)
	{
	}
}
