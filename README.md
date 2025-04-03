PCD a.y. 2024-2025 - ISI LM UNIBO - Cesena Campus

❤ Implemented with love and passion by: 
* 💻 Giacomo Foschi, giacomo.foschi3@studio.unibo.it
* 💻 Giovanni Pisoni, giovanni.pisoni@studio.unibo.it
* 💻 Giovanni Rinchiuso, giovanni.rinchiuso@studio.unibo.it
* 💻 Gioele Santi, gioele.santi2@studio.unibo.it

## 🐦🐦🐦🐦 Assignment #01 -  Concurrent Boids 🐦🐦🐦🐦

The assignment is about designing and developing a concurrent version of the original [boids simulation](https://en.wikipedia.org/wiki/Boids), as conceived by Craig Reynolds in 1986.

A Java-based implementation with a Swing GUI is avaiable in this repo.

The objective of the assignment is to design and develop a concurrent version of Boid, using three different approaches (producing three different versions):
- Java **multithreaded programming** (using default/platform threads)
- **Task-based** approach based o Java **Executor Framework** 
- Java **Virtual Threads** 

The GUI provide:
- buttons to start/stop the simulation
- input box to specify at the beginning the number of boids to be used
- sliders to define the weights for separation/alignment/cohesion 

Remarks:
- Every version (multithreaded, task/executor, virtual thread) should exploit as much as possible the specific key features of the mechanisms/abstractions provided by the approach, both at the design and implementation level. 
- All versions should promote modularity, encapsulation as well as performance, reactivity. 
- For active components/process interaction, prefer the use of higher level constructs (such as monitors) with respecto to lower level (e.g. `synchronized` blocks). 
- A different language than Java can be used: however, in that case, be sure to identify/adopt/implement equivalent frameworks/mechanisms (threads, tasks, virtual threads) for each version.

For every aspect not specified, students are free to choose the best approach for them.
