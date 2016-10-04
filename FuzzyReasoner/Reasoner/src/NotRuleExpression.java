import java.util.ArrayList;

/**
 * NOT evaluation of a natural expression
 */
public class NotRuleExpression extends RuleExpression {
	
	private RuleExpression expression;
	
	/** Constructor */
	public NotRuleExpression(RuleExpression expression) {
		this.expression = expression;
	}

	/**
	 * Returns the NOT (1-x) of a natural expression
	 */
	@Override
	public double evaluateExpression(ArrayList<MembershipFunction> inputs) {
		return 1 - expression.evaluateExpression(inputs);
	}

}
