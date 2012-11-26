package ai;

import java.util.ArrayList;
import java.util.List;

public class FlightPath
{
	private static List<Node> instance;

	public static List<Node> getInstance()
	{
		if (instance == null)
		{
			instance = new ArrayList<Node>();
		}

		return instance;
	}

	public static void setInstance(List<Node> instance)
	{
		FlightPath.instance = instance;
	}	
}
