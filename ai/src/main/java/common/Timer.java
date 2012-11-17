package common;

public class Timer
{
	private static long ONE_BILLION = 1000000000L;

	private static long ONE_MILLION = 1000000L;

	private long deltaTime;

	private long elapsedTime;

	private long startTime;

	public float getDeltaTime()
	{
		return (float) deltaTime / ONE_BILLION;
	}

	public float getElapsedTime()
	{
		return (float) elapsedTime / ONE_BILLION;
	}

	public void start()
	{
		deltaTime = 0L;
		elapsedTime = 0L;
		startTime = System.nanoTime();
	}

	public void tick()
	{
		long time = System.nanoTime();
		deltaTime = time - (startTime + elapsedTime);
		elapsedTime = time - startTime;
	}

	public void waitUntilDeltaReaches(float deltaTime)
	{
		long currentDeltaTime = System.nanoTime() - (startTime + elapsedTime);
		long targetDeltaTime = (long) (deltaTime * ONE_BILLION);

		if (targetDeltaTime > currentDeltaTime)
		{
			try
			{
				Thread.sleep((targetDeltaTime - currentDeltaTime) / ONE_MILLION);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
}
