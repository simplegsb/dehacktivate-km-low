package common;

public class Vectorf2
{
	public static Vectorf2 ZERO_HEADING = new Vectorf2(1.0f, 0.0f);

	public static Vectorf2 add(Vectorf2 lhs, Vectorf2 rhs)
	{
		Vectorf2 sum = lhs.copy();
		sum.add(rhs);

		return sum;
	}

	public static Vectorf2 divide(Vectorf2 lhs, float scalar)
	{
		Vectorf2 product = lhs.copy();
		product.divide(scalar);

		return product;
	}

	public static Vectorf2 divide(Vectorf2 lhs, Vectorf2 rhs)
	{
		Vectorf2 product = lhs.copy();
		product.divide(rhs);

		return product;
	}

	public static float dotProduct(Vectorf2 lhs, Vectorf2 rhs)
	{
		float dot = 0.0f;

		dot += lhs.x * rhs.x;
		dot += lhs.y * rhs.y;

		return dot;
	}

	public static float crossProduct(Vectorf2 lhs, Vectorf2 rhs)
	{
		return lhs.x * rhs.x + lhs.y * rhs.y;
	}

	public static Vectorf2 multiply(Vectorf2 lhs, float scalar)
	{
		Vectorf2 product = lhs.copy();
		product.multiply(scalar);

		return product;
	}

	public static Vectorf2 multiply(Vectorf2 lhs, Vectorf2 rhs)
	{
		Vectorf2 product = lhs.copy();
		product.multiply(rhs);

		return product;
	}

	public static float getProximity(Vectorf2 lhs, Vectorf2 rhs)
	{
		Vectorf2 difference = lhs;
		difference.subtract(rhs);

		return difference.getMagnitude();
	}

	public static Vectorf2 subtract(Vectorf2 lhs, Vectorf2 rhs)
	{
		Vectorf2 sum = lhs.copy();
		sum.subtract(rhs);

		return sum;
	}

	public float x;

	public float y;

	public Vectorf2()
	{
		x = 0.0f;
		y = 0.0f;
	}

	public Vectorf2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public void add(Vectorf2 rhs)
	{
		x += rhs.x;
		y += rhs.y;
	}

	public float angleTo(Vectorf2 rhs)
	{
		return (float) Math.acos(dotProduct(this, rhs) / (getMagnitude() * rhs.getMagnitude()));
	}

	public Vectorf2 copy()
	{
		return new Vectorf2(x, y);
	}

	public void divide(float scalar)
	{
		x /= scalar;
		y /= scalar;
	}

	public void divide(Vectorf2 rhs)
	{
		x /= rhs.x;
		y /= rhs.y;
	}

	public boolean equals(Vectorf2 rhs)
	{
		return x == rhs.x && y == rhs.y;
	}

	public float getMagnitude()
	{
		return (float) Math.sqrt(getMagnitudeSquared());
	}

	public float getMagnitudeSquared()
	{
		float magnitudeSquared = 0.0f;

		magnitudeSquared += Math.pow(x, 2);
		magnitudeSquared += Math.pow(y, 2);

		return magnitudeSquared;
	}

	public float getRotation()
	{
		return (float) Math.acos(dotProduct(ZERO_HEADING, this) / (1.0f * getMagnitude()));
	}

	public void multiply(float scalar)
	{
		x *= scalar;
		y *= scalar;
	}

	public void multiply(Vectorf2 rhs)
	{
		x *= rhs.x;
		y *= rhs.y;
	}

	public void negate()
	{
		multiply(-1.0f);
	}

	public void normalize()
	{
		multiply(1.0f / getMagnitude());
	}

	public void rotate(float angle)
	{
		float cosine = (float) Math.cos(angle);
		float sine = (float) Math.sin(angle);

		float tempX = x * cosine - y * sine;
		y = x * sine + y * cosine;
		x = tempX;
	}

	public void setToUnitRotation(float radians)
	{
		x = (float) Math.sin(Math.PI - radians);
		y = (float) Math.cos(Math.PI -radians);
	}

	public void subtract(Vectorf2 rhs)
	{
		x -= rhs.x;
		y -= rhs.y;
	}

	public String toString()
	{
		return "[" + x + "," + y + "]";
	}
}
