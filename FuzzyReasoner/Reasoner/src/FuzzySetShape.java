
/**
 * Abstract model of a fuzzy set
 */
public abstract class FuzzySetShape {
	private String attributeName;
	protected double clip;  // Clip for fuzzy sets in output functions
	private double mu;  // (μ) Result of fuzzification of the input
	
	/** Constructor */
	public FuzzySetShape(String attributeName) {
		this.attributeName = attributeName;
		clip = 1;
		mu = 0;
	}
	
	/** Getter of the attribute name */
	public String getAttributeName() {
		return attributeName;
	}
	
	/** Setter of the clip */
	public void setClip(double clip) {
		this.clip = clip;
	}
	
	/** Getter of the clip */
	public double getClip() {
		return clip;
	}
	
	/** Getter of Mu (μ) */
	public double getMu() {
		return mu;
	}

	/** Setter of Mu (μ) */
	public void setMu(double mu) {
		this.mu = mu;
	}

	/**
	 * Fuzzificates the input on the fuzzy set, i.e. calculates how much
	 * a given input (position) belongs to a fuzzy set. 
	 */
	public abstract double fuzzificateInput(double position);
	
}
