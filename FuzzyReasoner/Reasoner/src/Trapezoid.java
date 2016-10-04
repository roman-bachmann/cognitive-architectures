
/**
 * Trapezoid fuzzy set.
 * x0 is the start of the positive incline, x1 the end.
 * Between x1 and x2, the value is 1.
 * x2 is the start of the negative incline, x3 the end.
 */
public class Trapezoid extends FuzzySetShape {
	
	private double x0, x1, x2, x3;
	
	/** Constructor */
	public Trapezoid(String attributeName, double x0, double x1, double x2, double x3) {
		super(attributeName);
		this.x0 = x0;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
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
	
	/** Getter of x3 */
	public double getX3() {
		return x3;
	}

	@Override
	public double fuzzificateInput(double position) {
		double value = 0.0;
		if (position > x0 && position < x1) 
			value = (position - x0) / (x1 - x0);
		else if (position >= x1 && position <= x2)
			value = 1;
		else if (position > x2 && position < x3)
			value = (x3 - position) / (x3 - x2);
		return value;
	}

}
