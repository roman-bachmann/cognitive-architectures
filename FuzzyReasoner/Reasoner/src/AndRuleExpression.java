import java.util.ArrayList;

/**
 * AND evaluation of two natural expressions
 */
public class AndRuleExpression extends RuleExpression {
	private RuleExpression left, right;
	
	/** Constructor */
	public AndRuleExpression(RuleExpression left, RuleExpression right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the AND (min) of two natural expressions
	 */
	@Override
	public double evaluateExpression(ArrayList<MembershipFunction> inputs) {
		return Math.min(left.evaluateExpression(inputs), right.evaluateExpression(inputs));
	}
}
