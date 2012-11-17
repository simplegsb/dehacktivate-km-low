package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JSON
{
	private static JsonConfig CONFIG = new JsonConfig();

	static
	{
		CONFIG.addIgnoreFieldAnnotation(JSONIgnore.class);
	}

	public static Collection<Instruction> fromInstructionFile(String fileName, int initialCapacity)
	{
		try
		{
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			StringBuffer buffer = new StringBuffer(initialCapacity);

			while (file.ready())
			{
				buffer.append(file.readLine());
			}
			file.close();

			JSONArray instructionsJson = JSONArray.fromObject(buffer.toString(), CONFIG);
			Collection<Instruction> instructions = new ArrayList<Instruction>();

			for (int instructionIndex = 0; instructionIndex < instructionsJson.size(); instructionIndex++)
			{
				JSONObject instructionJson = instructionsJson.getJSONObject(instructionIndex);
				Instruction instruction = new Instruction();

				JSONArray waypoints = instructionJson.getJSONArray("waypoints");
				for (int waypointIndex = 0; waypointIndex < waypoints.size(); waypointIndex++)
				{
					JSONObject waypoint = waypoints.getJSONObject(waypointIndex);
					instruction.waypoints.add((Vectorf2) JSONObject.toBean(waypoint, Vectorf2.class));
				}

				instructions.add(instruction);
			}

			return instructions;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static Data fromDataFile(String fileName, int initialCapacity)
	{
		try
		{
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			StringBuffer buffer = new StringBuffer(initialCapacity);

			while (file.ready())
			{
				buffer.append(file.readLine());
			}
			file.close();

			JSONObject json = JSONObject.fromObject(buffer.toString(), CONFIG);
			Data data = (Data) JSONObject.toBean(json, Data.class);

			JSONArray objects = json.getJSONArray("objects");
			for (int index = 0; index < objects.size(); index++)
			{
				JSONObject object = objects.getJSONObject(index);
				// Only planes have names!
				if (!object.containsKey("name"))
				{
					data.obstacles.add((Obstacle) JSONObject.toBean(object, Obstacle.class));
				}
				else
				{
					data.planes.add((Plane) JSONObject.toBean(object, Plane.class));
				}
			}

			return data;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <ObjectType> ObjectType fromObjectFile(Class<ObjectType> clazz, String fileName,
			int initialCapacity)
	{
		try
		{
			BufferedReader file = new BufferedReader(new FileReader(fileName));
			StringBuffer buffer = new StringBuffer(initialCapacity);

			while (file.ready())
			{
				buffer.append(file.readLine());
			}
			file.close();

			JSONObject json = JSONObject.fromObject(buffer.toString(), CONFIG);

			return (ObjectType) JSONObject.toBean(json, clazz);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static void toArrayFile(Object object, String fileName)
	{
		try
		{
			FileWriter file = (new FileWriter(fileName));
			file.write(JSONArray.fromObject(object, CONFIG).toString());
			file.flush();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void toObjectFile(Object object, String fileName)
	{
		try
		{
			FileWriter file = (new FileWriter(fileName));
			file.write(JSONObject.fromObject(object, CONFIG).toString());
			file.flush();
			file.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
