# Øving 2 – Kognitive Arkitekturer: Mental- programmering i NGOMSL

***Author: Roman Bachmann***


## a) Typical unit-tasks and top level in NGOMSL

### Typical unit-tasks in a telephone app

* Calling someone
	* Calling someone using dialler
		* Choosing mode Tastatur
		* Pressing a digit button
		* Pressing the call button
		* Verifying correct number
	* Calling someone using favourites
		* Choosing mode Favoritter
		* Finding contact
		* Press contact
* Pressing a button

### Top level model in NGOMSL

**Method to accomplish goal of _calling someone_**  
Step 1. Get next unit task information from marked-up manuscript  
Step 2. Decide: If no more unit tasks, then return with goal accomplished  Step 3. Accomplish the goal of _performing the unit task_  Step 4. Goto 1  

**Selection rule set for the goal of _performing the unit task_**  
If the task is calling someone using the dialler, then accomplish the goal of _calling someone using the dialler_  
If the task is calling someone using the favourites, then accomplish the goal of _calling someone using the favourites_  
If the task is pressing a button, then accomplish the goal of _pressing a button_  
If the task is choosing mode Tastatur, then accomplish the goal of _choosing mode Tastatur_  
If the task is choosing mode Favoritter, then accomplish the goal of _choosing mode Favoritter_  
If the task is pressing a number button, then accomplish the goal of _pressing a digit button_  
If the task is pressing the telephone button, then accomplish the goal of _pressing the call button_  
If the task is pressing the contact, then accomplish the goal of _pressing the contact_  
Return with goal accomplished


## b) The goal "call"

**Method to accomplish goal of _calling someone using the dialler_**  
Step 1. Accomplish goal of _*choosing mode Tastatur*_  
Step 2. Accomplish goal of _*pressing a digit button*_  
Step 3. Decide: If more digits in telephone number to type in, then goto 2  
Step 4. Accomplish goal of _*pressing the call button*_  
Step 5. Return with goal accomplished  

**Method to accomplish goal of _calling someone using the favourites_**  
Step 1. Accomplish goal of _*choosing mode Favoritter*_  
Step 2. Accomplish goal of _*pressing the contact*_  
Step 3. Return with goal accomplished  

**Method to accomplish goal of _pressing a button_**  
Step 1. *Recall* button action and *retrieve from LTM* the button symbol for it  
Step 2. *Locate* the button symbol  
Step 3. *Move* finger to it on the phone  
Step 4. *Press* finger on it  
Step 5. *Verify* that it is selected  
Step 6. *Release* finger  
Step 7. Return with goal accomplished  

**Method to accomplish goal of _choosing mode Tastatur_**  
Step 1. *Retain* that the button action is TASTATUR, and accomplish goal of _*pressing a button*_  
Step 2. Return with goal accomplished

**Method to accomplish goal of _choosing mode Favoritter_**  
Step 1. *Retain* that the button action is FAVORITTER, and accomplish goal of _*pressing a button*_  
Step 2. Return with goal accomplished

**Method to accomplish goal of _pressing a digit button_**  
Step 1. *Retain* next digit of telephone number  
Step 2. *Retain* that the button action is DIGIT, and accomplish goal of _*pressing a button*_  
Step 3. *Verify*, that correct digit was entered  
Step 4. Return with goal accomplished

**Method to accomplish goal of _pressing the call button_**  
Step 1. *Retain* that the button action is CALL, and accomplish goal of _*pressing a button*_  
Step 2. Return with goal accomplished

**Method to accomplish goal of _pressing the contact_**  
Step 1. *Retrieve from LTM* contact name  
Step 2. *Locate* the contact  
Step 3. *Move* finger to it on the phone  
Step 4. *Press* finger on it   
Step 5. *Verify* that it is selected  
Step 6. *Release* finger  
Step 7. Return with goal accomplished  

## c) Execution times

CP = 1.2, B = 0.1, H = 0.4, K = 0.2, M = 1.2, P = 1.1, 1 statement = 0.1 [seconds]


### Execution time using dialler

* T(*calling someone using the dialler*) = 5 statements = 5 * 0.1s = 0.5s
	* T(*choosing mode Tastatur*) = 2 statements + M = 2 * 0.1s + 1.2s = 1.4s
		* T(*pressing a button*) = 7 statements + 3\*M + CP + P + 2\*B = 0.7s + 3.6s + 1.2s + 1.1s + 0.2s = 6.8s
	* T(*pressing a digit button*) = 4 statements + 3*M = 0.4s + 3.6s = 4s
		* 8\*T(*pressing a button*) = 8\* 6.8s = 54.4s
	* T(*pressing the call button*) = 2 statements + M = 2 * 0.1s + 1.2s = 1.4s 
		* T(*pressing a button*) = 6.8s

Total time = 0.5s + 1.4s + 6.8s + 4s + 54.4s + 1.4s + 6.8s = _75.3s_

### Execution time using favourites

* T(*calling someone using the favourites*) = 3 statements = 3 * 0.1 = 0.3s
	* T(*choosing mode Favoritter*) = 2 statements + M = 2 * 0.1s + 1.2s = 1.4s
		* T(*pressing a button*) = 7 statements + 3\*M + CP + P + 2\*B = 0.7s + 3.6s + 1.2s + 1.1s + 0.2s = 6.8s
	* T(*pressing the contact*) = 7 statements + 2\*M + CP + P + 2\*B = 0.7s + 2.4s + 1.2s + 1.1s + 0.2s = 5.6s

Total time = 0.3s + 1.4s + 6.8s + 5.6s = _14.1s_
