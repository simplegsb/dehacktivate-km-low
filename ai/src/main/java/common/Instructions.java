package common;

import java.util.ArrayList;
import java.util.List;

public class Instructions
{
	private static List<Instruction> instance;

	public static List<Instruction> getInstance()
	{
		if (instance == null)
		{
			instance = new ArrayList<Instruction>();
		}

		return instance;
	}

	public static void setInstance(List<Instruction> instance)
	{
		Instructions.instance = instance;
	}
}
