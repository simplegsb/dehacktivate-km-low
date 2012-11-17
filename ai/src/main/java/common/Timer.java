package common;

public class Timer
{
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
}
