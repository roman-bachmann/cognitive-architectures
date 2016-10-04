import java.util.ArrayList;

/**
 * Expression of the simplest level.
 * Models a "variableName" is "fuzzySetName" relationship
 */
public class NaturalExpression extends RuleExpression {
	
	public String variableName, fuzzySetName;
	
	/** Constructor */
	public NaturalExpression(String variable, String fuzzySet) {
		this.variableName = variable;
		this.fuzzySetName = fuzzySet;
	}
	
	/**
	 * Returns the degree on how much the variableName is in the fuzzySetName
	 */
	@Override
	public double evaluateExpression(ArrayList<MembershipFunction> inputs) {
		for (MembershipFunction f : inputs) {
			if (f.getFunctionName().equals(variableName)) {
				ArrayList<FuzzySetShape> shapes = f.getShapes();
				for (FuzzySetShape fss : shapes) {
					if (fss.getAttributeName().equals(fuzzySetName)) {
						return fss.getMu();
					}
				}
			}
		}
		
		return 0;
	}
}
