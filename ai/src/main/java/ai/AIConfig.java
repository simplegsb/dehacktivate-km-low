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

	public float boundaryRepelFactor;

	public String dataFilePath;

	public float destinationThreshold;

	public float escapeTime;

	public String flightPathFilePath;

	public int frameRateCap;

	public String instructionsFilePath;

	public float obstacleRepelFactor;

	public String overrideFilePath;

	public float planeRepelFactor;

	public float repulsionStrengthFactor;

	public float waypointLeadTime;

	public float getBoundary_repel_factor()
	{
		return boundaryRepelFactor;
	}

	public String getData_file_path()
	{
		return dataFilePath;
	}

	public float getDestination_threshold()
	{
		return destinationThreshold;
	}

	public float getEscape_time()
	{
		return escapeTime;
	}

	public String getFlight_path_file_path()
	{
		return flightPathFilePath;
	}

	public int getFrame_rate_cap()
	{
		return frameRateCap;
	}

	public String getInstructions_file_path()
	{
		return instructionsFilePath;
	}

	public float getObstacle_repel_factor()
	{
		return obstacleRepelFactor;
	}

	public String getOverride_file_path()
	{
		return overrideFilePath;
	}

	public float getPlane_repel_factor()
	{
		return planeRepelFactor;
	}

	public float getRepulsion_strength_factor()
	{
		return repulsionStrengthFactor;
	}

	public float getWaypoint_lead_time()
	{
		return waypointLeadTime;
	}

	public void setBoundary_repel_factor(float boundary_repel_factor)
	{
		this.boundaryRepelFactor = boundary_repel_factor;
	}

	public void setData_file_path(String data_file_path)
	{
		this.dataFilePath = data_file_path;
	}

	public void setDestination_threshold(float destination_threshold)
	{
		this.destinationThreshold = destination_threshold;
	}

	public void setEscape_time(float escape_time)
	{
		this.escapeTime = escape_time;
	}

	public void setFlight_path_file_path(String flight_path_file_path)
	{
		this.flightPathFilePath = flight_path_file_path;
	}

	public void setFrame_rate_cap(int frame_rate_cap)
	{
		this.frameRateCap = frame_rate_cap;
	}

	public void setInstructions_file_path(String instructions_file_path)
	{
		this.instructionsFilePath = instructions_file_path;
	}

	public void setObstacle_repel_factor(float obstacle_repel_factor)
	{
		this.obstacleRepelFactor = obstacle_repel_factor;
	}

	public void setOverride_file_path(String override_file_path)
	{
		this.overrideFilePath = override_file_path;
	}

	public void setPlane_repel_factor(float plane_repel_factor)
	{
		this.planeRepelFactor = plane_repel_factor;
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
