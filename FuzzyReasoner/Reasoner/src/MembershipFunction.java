import java.util.ArrayList;

/**
 * Model of a membership function made up of fuzzy sets
 */
public class MembershipFunction {
	private String functionName;
	private ArrayList<FuzzySetShape> shapes;
	
	/** Constructor */
	public MembershipFunction(String functionName) {
		this.functionName = functionName;
		shapes = new ArrayList<>();
	}
	
	/**
	 * Adds a specified fuzzy set to the collection of set
	 */
	public void addShape(FuzzySetShape shape) {
		shapes.add(shape);
	}

	/**
	 * Getter for function name
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * Getter for fuzzy shapes
	 */
	public ArrayList<FuzzySetShape> getShapes() {
		return shapes;
	}
	
	/**
	 * Sets the Mu of all fuzzy sets for a specified input position
	 * 
	 * @param position
	 */
	public void setMus(double position) {
		for (FuzzySetShape fss : shapes) {
			fss.setMu(fss.fuzzificateInput(position));
		}
	}
}
