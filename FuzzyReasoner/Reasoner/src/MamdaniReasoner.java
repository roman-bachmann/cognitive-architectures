import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class of the Mamdani reasoner
 */
public class MamdaniReasoner {

	public static void main(String[] args) {
		
		// Input 1
		MembershipFunction distance = new MembershipFunction("distance");
		distance.addShape(new ReverseGrade("VerySmall", 1, 2.5));
		distance.addShape(new Triangle("Small", 1.5, 3, 4.5));
		distance.addShape(new Triangle("Perfect", 3.5, 5, 6.5));
		distance.addShape(new Triangle("Big", 5.5, 7, 8.5));
		distance.addShape(new Grade("VeryBig", 7.5, 9));
		
		// Input 2
		MembershipFunction delta = new MembershipFunction("delta");
		delta.addShape(new ReverseGrade("ShrinkingFast", -4, -2.5));
		delta.addShape(new Triangle("Shrinking", -3.5, -2, -0.5));
		delta.addShape(new Triangle("Stable", -1.5, 0, 1.5));
		delta.addShape(new Triangle("Growing", 0.5, 2, 3.5));
		delta.addShape(new Grade("GrowingFast", 2.5, 4));
		
		ArrayList<MembershipFunction> inputs = new ArrayList<>();
		inputs.add(distance);
		inputs.add(delta);
		
		
		// Action
		MembershipFunction action = new MembershipFunction("action");
		action.addShape(new ReverseGrade("BrakeHard", -8, -5));
		action.addShape(new Triangle("SlowDown", -7, -4, -1));
		action.addShape(new Triangle("None", -3, 0, 3));
		action.addShape(new Triangle("SpeedUp", 1, 4, 7));
		action.addShape(new Grade("FloorIt", 5, 8));
		
		ArrayList<MembershipFunction> outputs = new ArrayList<>();
		outputs.add(action);
		
		
		// Rules
		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(
				new AndRuleExpression(
						new NaturalExpression("distance", "Small"), 
						new NaturalExpression("delta", "Growing")), 
				"action", 
				"None"));
		rules.add(new Rule(
				new AndRuleExpression(
						new NaturalExpression("distance", "Small"), 
						new NaturalExpression("delta", "Stable")), 
				"action", 
				"SlowDown"));
		rules.add(new Rule(
				new AndRuleExpression(
						new NaturalExpression("distance", "Perfect"), 
						new NaturalExpression("delta", "Growing")), 
				"action", 
				"SpeedUp"));
		rules.add(new Rule(
				new AndRuleExpression(
						new NaturalExpression("distance", "VeryBig"), 
						new OrRuleExpression(
								new NotRuleExpression(new NaturalExpression("delta", "Growing")), 
								new NotRuleExpression(new NaturalExpression("delta", "GrowingFast")))), 
				"action", 
				"FloorIt"));
		rules.add(new Rule(
				new NaturalExpression("distance", "VerySmall"),
				"action",
				"BrakeHard"));
		
		
		// Get input values from user
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the distance position: ");
		double distancePosition = scanner.nextDouble();
		System.out.print("Enter the delta position: ");
		double deltaPosition = scanner.nextDouble();
		scanner.close();
		
		
		// Set Mu's (μ)
		distance.setMus(distancePosition);
		delta.setMus(deltaPosition);
		
		
		// Evaluate rules and update clips
		for (Rule r : rules) {
			r.evaluateRule(inputs, outputs);
		}
		
		
		// Calculate center of gravity (cog)
		double actionCOG = cog(-10, 10, 1, action);
		System.out.println("COG = " + actionCOG);
		
	}
	
	/**
	 * Calculates the center of gravity from a specified function.
	 * Uses discrete points to calculate it.
	 * 
	 * @param lowerBound Starting point for sampling
	 * @param upperBound End point for sampling
	 * @param stepSize Distance between two sampling points
	 * @param out The desired output function
	 * @return Center of gravity
	 */
	private static double cog(double lowerBound, double upperBound, double stepSize, MembershipFunction out) {
		if (lowerBound > upperBound)
			return 0;
		
		double productSum = 0;
		double clipSum = 0;
		
		for (double pos = lowerBound; pos <= upperBound; pos += stepSize) {
			double max = 0;
			for (FuzzySetShape fss : out.getShapes()) {
				double x = fss.fuzzificateInput(pos);
				max = (x > max) ? x : max;  // Taking OR (max) of overlapping fuzzy sets
			}
			productSum += max * pos;
			clipSum += max;
			System.out.println(max);
		}
		
		return productSum / clipSum;
	}
}
