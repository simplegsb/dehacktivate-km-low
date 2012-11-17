package common;

import java.util.ArrayList;
import java.util.List;

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

	public Instructions()
	{
		instructions = new ArrayList<Instruction>();
	}

	public List<Instruction> instructions;

	public List<Instruction> getInstructions()
	{
		return instructions;
	}

	public void setInstructions(List<Instruction> instructions)
	{
		this.instructions = instructions;
	}
}
