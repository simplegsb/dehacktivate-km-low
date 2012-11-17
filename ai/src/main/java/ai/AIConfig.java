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

	public float repulsion_strength_factor;

	public float steering_waypoint_lead_factor;

	public float getRepulsion_strength_factor()
	{
		return repulsion_strength_factor;
	}

	public float getSteering_waypoint_lead_factor()
	{
		return steering_waypoint_lead_factor;
	}

	public void setRepulsion_strength_factor(float repulsion_strength_factor)
	{
		this.repulsion_strength_factor = repulsion_strength_factor;
	}

	public void setSteering_waypoint_lead_factor(float steering_waypoint_lead_factor)
	{
		this.steering_waypoint_lead_factor = steering_waypoint_lead_factor;
	}
}
