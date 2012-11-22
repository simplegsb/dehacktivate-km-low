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

	public String dataFilePath;

	public String flightPathFilePath;

	public int frameRateCap;

	public String instructionsFilePath;

	public float lowFuelThreshold;

	public String overrideFilePath;

	public float repulsionStrengthFactor;

	public float waypointLeadTime;

	public String getData_file_path()
	{
		return dataFilePath;
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

	public float getLow_fuel_threshold()
	{
		return lowFuelThreshold;
	}

	public String getOverride_file_path()
	{
		return overrideFilePath;
	}

	public float getRepulsion_strength_factor()
	{
		return repulsionStrengthFactor;
	}

	public float getWaypoint_lead_time()
	{
		return waypointLeadTime;
	}

	public void setData_file_path(String data_file_path)
	{
		this.dataFilePath = data_file_path;
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

	public void setLow_fuel_threshold(float low_fuel_threshold)
	{
		this.lowFuelThreshold = low_fuel_threshold;
	}

	public void setOverride_file_path(String override_file_path)
	{
		this.overrideFilePath = override_file_path;
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
