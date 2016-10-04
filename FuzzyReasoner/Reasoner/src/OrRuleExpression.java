import java.util.ArrayList;

/**
 * OR evaluation of two natural expressions
 */
public class OrRuleExpression extends RuleExpression {

	private RuleExpression left, right;
	
	/** Constructor */
	public OrRuleExpression(RuleExpression left, RuleExpression right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the OR (max) of two natural expressions
	 */
	@Override
	public double evaluateExpression(ArrayList<MembershipFunction> inputs) {
		return Math.max(left.evaluateExpression(inputs), right.evaluateExpression(inputs));
	}

}
