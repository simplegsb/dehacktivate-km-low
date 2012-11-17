package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JSON
{
	private static JsonConfig CONFIG = new JsonConfig();

	static
	{
		CONFIG.addIgnoreFieldAnnotation(JSONIgnore.class);
	}

	@SuppressWarnings("unchecked")
	public static <ObjectType> ObjectType fromFile(Class<ObjectType> clazz, String fileName,
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

	public static void toFile(Object object, String fileName)
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
