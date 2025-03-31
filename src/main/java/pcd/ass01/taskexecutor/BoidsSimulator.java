package pcd.ass01.taskexecutor;

import java.util.Optional;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;
    private boolean running = true;
    private boolean paused = false;
    
    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
    	while (running) {
            var t0 = System.currentTimeMillis();
    		var boids = model.getBoids();


    		for (Boid boid : boids) {
                boid.update(model);
            }

            /*
             * Improved correctness: first update velocities...
             */
            for (Boid boid : boids) {
                boid.updateVelocity(model);
            }

            /*
             * ..then update positions
             */
            for (Boid boid : boids) {
                boid.updatePos(model);
            }

            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var frameRatePeriod = 1000 / FRAMERATE;

                if (dtElapsed < frameRatePeriod) {
                    try {
                        Thread.sleep(frameRatePeriod - dtElapsed);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
            
    	}
    }

    public void stopSimulation() {
        running = false;
    }

    public void pauseSimulation() {
        this.paused = true;
    }

    public void resumeSimulation() {
        this.paused = false;
    }
}
