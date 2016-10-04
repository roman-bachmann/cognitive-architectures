import java.util.ArrayList;

/**
 * Model of a rule. A rule has one rule expression ("if" side of the rule)
 * and an outputVariableName and outputFuzzySetName, that describe the action
 * ("then" side of rule).
 */
public class Rule {
	
	private RuleExpression expression;
	private String outputVariableName, outputFuzzySetName;
	
	/** Constructor */
	public Rule(RuleExpression expression, String outputVariableName, String outputFuzzySetName) {
		this.expression = expression;
		this.outputFuzzySetName = outputFuzzySetName;
		this.outputVariableName = outputVariableName;
	}
	
	/**
	 * Set all the clips in the output functions according to the rule and the input functions
	 * 
	 * @param inputs Collection of input functions
	 * @param outputs Collection of output functions
	 */
	public void evaluateRule(ArrayList<MembershipFunction> inputs, ArrayList<MembershipFunction> outputs) {
		double clip = expression.evaluateExpression(inputs);
		
		for (MembershipFunction f : outputs) {
			if (f.getFunctionName().equals(outputVariableName)) {
				ArrayList<FuzzySetShape> shapes = f.getShapes();
				for (FuzzySetShape fss : shapes) {
					if (fss.getAttributeName().equals(outputFuzzySetName)) {
						fss.setClip(clip);
					}
				}
			}
		}
	}
	
}
