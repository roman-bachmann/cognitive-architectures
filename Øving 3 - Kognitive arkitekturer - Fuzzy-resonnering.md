# Øving 3 - Kognitive arkitekturer : Fuzzy-resonnering

***Author: Roman Bachmann***

## a) Manual Mamdani reasoning

### Step 1: Fuzzification of the input variables

Distance:

* μ(distance = VerySmall) = 0
* μ(distance = Small) = (4.5 - 3.7) / (4.5 - 3) = 0.53
* μ(distance = Perfect) = (3.7 - 3.5) / (5 - 3.5) = 0.13
* μ(distance = Big) = 0
* μ(distance = VeryBig) = 0

Delta:

* μ(delta = ShrinkingFast) = 0
* μ(delta = Shrinking) = 0
* μ(delta = Stable) = (1.5 - 1.2) / (1.5 - 0) = 0.2
* μ(delta = Growing) = (1.2 - 0.5) / (2 - 0.5) = 0.47
* μ(delta = GrowingFast) = 0

### Step 2: Rule evaluation

* IF distance is Small (0.53) AND delta is Growing (0.47) THEN action is None (min=0.47)
* IF distance is Small (0.53) AND delta is Stable (0.2) THEN action is SlowDown (min=0.2)
* IF distance is Perfect (0.13) AND delta is Growing (0.47) THEN action is SpeedUp (min=0.13)* IF distance is VeryBig (0) AND (delta is NOT Growing (1-0.47=0.53) OR delta is NOT GrowingFast (1-0=1)) (max=1) THEN action is FloorIt (min=0)* IF distance is VerySmall (0) THEN action is BrakeHard (0)

### Step 3: Aggregation of the rule outputs

Cap the following action:

* BrakeHard at 0
* SlowDown at 0.2
* None at 0.47
* SpeedUp at 0.13
* FloorIt at 0

### Step 4: Defuzzification

Calculating the center of gravity of the aggregated fuzzy set:
```
COG = ((-6-5-4-3)*0.2 + (-2)*0.33 + (-1+0+1)*0.47 + (2)*0.33 + (3+4+5+6)*0.13) /
(4*0.2 + 0.33 + 3*0.47 + 0.33 + 4*0.13) = -0.37
```
According to this reasoning, the robot should take action None.

## b) Mamdani reasoner in Java

Since my code is split over several files, I also attached a zip archive of all the .java files, as well as the pdf version of this markdown document.

### MamdaniReasoner.java

```
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
```

### MembershipFunction.java

```
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
```

### FuzzySetShape.java

```

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
```

### Grade.java

```

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
```

### ReverseGrade.java

```

/**
 * Reverse grade fuzzy set.
 * x0 is the start of the negative incline, x1 the end.
 */
public class ReverseGrade extends FuzzySetShape {
	
	private double x0, x1;

	/** Constructor */
	public ReverseGrade(String attributeName, double x0, double x1) {
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
		if (position <= x0)
			value = 1.0;
		else if (position >= x1)
			value = 0.0;
		else
			value = (x1 - position) / (x1 - x0);
		if (value > clip)
			value = clip;
		return value;
	}

}
```

### Triangle.java

```

/**
 * Triangle fuzzy set.
 * x0 is the start of the positive incline, x1 the end.
 * x1 is the start of the negative incline, x2 the end.
 */
public class Triangle extends FuzzySetShape {
	
	private double x0, x1, x2;

	/** Constructor */
	public Triangle(String attributeName, double x0, double x1, double x2) {
		super(attributeName);
		this.x0 = x0;
		this.x1 = x1;
		this.x2 = x2;
	}
	
	/** Getter of x0 */
	public double getX0() {
		return x0;
	}

	/** Getter of x1 */
	public double getX1() {
		return x1;
	}

	/** Getter of x2 */
	public double getX2() {
		return x2;
	}

	@Override
	public double fuzzificateInput(double position) {
		double value = 0.0;
		if (position >= x0 && position <= x1)
			value = (position - x0) / (x1 - x0);
		else if (position >= x1 && position <= x2)
			value = (x2 - position) / (x1 - x0);
		if (value > clip)
			value = clip;
		return value;
	}

}
```

### Trapezoid.java

```

/**
 * Trapezoid fuzzy set.
 * x0 is the start of the positive incline, x1 the end.
 * Between x1 and x2, the value is 1.
 * x2 is the start of the negative incline, x3 the end.
 */
public class Trapezoid extends FuzzySetShape {
	
	private double x0, x1, x2, x3;
	
	/** Constructor */
	public Trapezoid(String attributeName, double x0, double x1, double x2, double x3) {
		super(attributeName);
		this.x0 = x0;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
	}

	/** Getter of x0 */
	public double getX0() {
		return x0;
	}

	/** Getter of x1 */
	public double getX1() {
		return x1;
	}

	/** Getter of x2 */
	public double getX2() {
		return x2;
	}
	
	/** Getter of x3 */
	public double getX3() {
		return x3;
	}

	@Override
	public double fuzzificateInput(double position) {
		double value = 0.0;
		if (position > x0 && position < x1) 
			value = (position - x0) / (x1 - x0);
		else if (position >= x1 && position <= x2)
			value = 1;
		else if (position > x2 && position < x3)
			value = (x3 - position) / (x3 - x2);
		return value;
	}

}
```

### Rule.java

```
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
```

### RuleExpression.java

```
import java.util.ArrayList;

/**
 * Abstract model of a expression of a rule (if part)
 */
public abstract class RuleExpression {

	public abstract double evaluateExpression(ArrayList<MembershipFunction> inputs);
	
}
```

### NaturalExpression.java

```
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
```

### AndRuleExpression.java

```
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
```

### OrRuleExpression.java

```
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
```

### NotRuleExpression.java

```
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
```
