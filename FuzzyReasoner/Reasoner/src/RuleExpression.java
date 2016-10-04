import java.util.ArrayList;

/**
 * Abstract model of a expression of a rule (if part)
 */
public abstract class RuleExpression {

	public abstract double evaluateExpression(ArrayList<MembershipFunction> inputs);
	
}
