# Øving 1 - Kognitive Arkitekturer - MHP/Fitts lov

***Author: Roman Bachmann***

a) 

* Right when the break signal lights up, we have T = 0.
* P fetches the colour into VIS. After a short delay, a visual representation of the colour is available in WM. T = τP
* C generates a motor command (press break pedal). T = τP + τC
* M executes the motor command controlling muscles that push down the break pedal. Total time becomes: T = τP + τC + τM = 240ms

b) 

* Right when the flag appears, we have T = 0
* P fetches the symbol into VIS. After a short delay, a visual representation of the flag is available in WM. T = τP
* C fetches the meaning from the LTM. T = τP + τC
* C categorises the flag and after that the user knows it is Scandinavian. T = τP + 2*τC = 240ms

c) ID = log2(D/S + 0.5), where D is the distance to the target and S is the size of the target. It quantifies the difficulty of a target selection task. The bigger D or the smaller S, the more difficult it gets to hit the target and the task needs more time to get accomplished.

* Shannon's version of Fitts law: T = a + b*log2(D/S + 1)
* We have a = 50 and b = 150. The distance D to the menu bar is D = 80 mm. SWin = 5mm, SMac = 50mm
* Macintosh: T = a + b * log2(D/SMac + 1) = 50 + 150 * log2(80mm/50mm + 1) = 256.78 ms
* Windows: T = a + b * log2(D/SWin + 1) = 50 + 150 * log2(80mm/5mm + 1) = 663.12 ms

d) Humans need to see at least around 24 pictures per second to get an illusion of continuous time.