package common;

import java.util.ArrayList;
import java.util.Collection;

public class Instructions
{
	private static Collection<Instruction> instance;

	public static Collection<Instruction> getInstance()
	{
		if (instance == null)
		{
			instance = new ArrayList<Instruction>();
		}

		return instance;
	}

	public static void setInstance(Collection<Instruction> instance)
	{
		Instructions.instance = instance;
	}
}
