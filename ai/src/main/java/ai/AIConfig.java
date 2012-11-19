package ai;

public class AIConfig
{
	private static AIConfig instance;

	public static AIConfig getInstance()
	{
		if (instance == null)
		{
			instance = new AIConfig();
		}

		return instance;
	}

	public static void setInstance(AIConfig instance)
	{
		AIConfig.instance = instance;
	}

	public int frameRateCap;

	public float repulsionStrengthFactor;

	public float waypointLeadTime;

	public int getFrame_rate_cap()
	{
		return frameRateCap;
	}

	public float getRepulsion_strength_factor()
	{
		return repulsionStrengthFactor;
	}

	public float getWaypoint_lead_time()
	{
		return waypointLeadTime;
	}

	public void setFrame_rate_cap(int frame_rate_cap)
	{
		this.frameRateCap = frame_rate_cap;
	}

	public void setRepulsion_strength_factor(float repulsion_strength_factor)
	{
		this.repulsionStrengthFactor = repulsion_strength_factor;
	}

	public void setWaypoint_lead_time(float waypoint_lead_time)
	{
		this.waypointLeadTime = waypoint_lead_time;
	}
}
