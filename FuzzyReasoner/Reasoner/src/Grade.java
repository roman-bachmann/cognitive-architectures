
/**
 * Grade fuzzy set.
 * x0 is the start of the positive incline, x1 the end.
 */
public class Grade extends FuzzySetShape {

	private double x0, x1;

	/** Constructor */
	public Grade(String attributeName, double x0, double x1) {
		super(attributeName);
		this.x0 = x0;
		this.x1 = x1;
	}
	
	/** Getter of x0 */
	public double getX0() {
		return x0;
	}

	/** Getter of x1 */
	public double getX1() {
		return x1;
	}
	
	@Override
	public double fuzzificateInput(double position) {
		double value = 0.0;
		if (position >= x1)
			value = 1.0;
		else if (position <= x0)
			value = 0.0;
		else
			value = (position -x0) / (x1 - x0);
		if (value > clip) 
			value = clip;
		return value;
	}
}
