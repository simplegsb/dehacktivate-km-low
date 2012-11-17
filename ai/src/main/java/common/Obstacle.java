package common;

public class Obstacle extends MileHighObject
{
	public Rectangle boundary;

	public Rectangle getBoundary()
	{
		return boundary;
	}

	public String getType()
	{
		return "obstacle";
	}

	public void setBoundary(Rectangle boundary)
	{
		this.boundary = boundary;
	}
}
