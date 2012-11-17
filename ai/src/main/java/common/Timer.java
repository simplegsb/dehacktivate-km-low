package common;

public class Timer
{
	private static long ONE_BILLION = 1000000000L;

	private long deltaTime;

	private long elapsedTime;

	private long startTime;

	public long getDeltaTime()
	{
		return deltaTime;
	}

	public long getElapsedTime()
	{
		return elapsedTime;
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

	public void waitUntilDeltaReaches(float fractionOfASecond)
	{
		long currentDeltaTime = System.nanoTime() - (startTime + elapsedTime);
		long targetDeltaTime = (long) (fractionOfASecond * ONE_BILLION);

		try
		{
			Thread.sleep(targetDeltaTime - currentDeltaTime);
		}
		catch (InterruptedException e)
		{
		}
	}
}
