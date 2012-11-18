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

	public int frame_rate_cap;

	public float repulsion_strength_factor;

	public float waypoint_lead_time;

	public int getFrame_rate_cap()
	{
		return frame_rate_cap;
	}

	public float getRepulsion_strength_factor()
	{
		return repulsion_strength_factor;
	}

	public float getWaypoint_lead_time()
	{
		return waypoint_lead_time;
	}

	public void setFrame_rate_cap(int frame_rate_cap)
	{
		this.frame_rate_cap = frame_rate_cap;
	}

	public void setRepulsion_strength_factor(float repulsion_strength_factor)
	{
		this.repulsion_strength_factor = repulsion_strength_factor;
	}

	public void setWaypoint_lead_time(float waypoint_lead_time)
	{
		this.waypoint_lead_time = waypoint_lead_time;
	}
}
