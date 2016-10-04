
/**
 * Triangle fuzzy set.
 * x0 is the start of the positive incline, x1 the end.
 * x1 is the start of the negative incline, x2 the end.
 */
public class Triangle extends FuzzySetShape {
	
	private double x0, x1, x2;

	/** Constructor */
	public Triangle(String attributeName, double x0, double x1, double x2) {
		super(attributeName);
		this.x0 = x0;
		this.x1 = x1;
		this.x2 = x2;
	}
	
	/** Getter of x0 */
	public double getX0() {
		return x0;
	}

	/** Getter of x1 */
	public double getX1() {
		return x1;
	}

	/** Getter of x2 */
	public double getX2() {
		return x2;
	}

	@Override
	public double fuzzificateInput(double position) {
		double value = 0.0;
		if (position >= x0 && position <= x1)
			value = (position - x0) / (x1 - x0);
		else if (position >= x1 && position <= x2)
			value = (x2 - position) / (x1 - x0);
		if (value > clip)
			value = clip;
		return value;
	}

}
