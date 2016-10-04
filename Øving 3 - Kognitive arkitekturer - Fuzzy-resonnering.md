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

COG = ((-6-5-4-3)\*0.2 + (-2)\*0.33 + (-1+0+1)\*0.47 + (2)\*0.33 + (3+4+5+6)\*0.13) / (4\*0.2 + 0.33 + 3\*0.47 + 0.33 + 4\*0.13) = -0.37

According to this reasoning, the robot should take action None.

## b) Mamdani reasoner in Java

