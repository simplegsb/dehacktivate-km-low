package simulator;

import common.Rectangle;
import common.Vectorf2;

public class SimulatorConfig
{
	private static SimulatorConfig instance;

	public static SimulatorConfig getInstance()
	{
		if (instance == null)
		{
			instance = new SimulatorConfig();
		}

		return instance;
	}

	public static void setInstance(SimulatorConfig instance)
	{
		SimulatorConfig.instance = instance;
	}

	public Rectangle boundary;

	public Vectorf2 runway;

	public float spawn_frequency;

	public Rectangle getBoundary() {
		return boundary;
	}

	public Vectorf2 getRunway() {
		return runway;
	}

	public float getSpawn_frequency()
	{
		return spawn_frequency;
	}

	public void setBoundary(Rectangle boundary) {
		this.boundary = boundary;
	}

	public void setRunway(Vectorf2 runway) {
		this.runway = runway;
	}

	public void setSpawn_frequency(float spawn_frequency)
	{
		this.spawn_frequency = spawn_frequency;
	}
}