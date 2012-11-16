package common;

public class Instructions
{
	private static Instructions instance;

	public static Instructions getInstance()
	{
		if (instance == null)
		{
			instance = new Instructions();
		}

		return instance;
	}

	public static void setInstance(Instructions instance)
	{
		Instructions.instance = instance;
	}
}
