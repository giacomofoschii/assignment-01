package pcd.ass01.taskexecutor;

import java.util.concurrent.*;

public class BoidsSimulator {

    private BoidsController boidsController;
    private final MyExecutor executor;
    private boolean running = true;

    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(final BoidsController boidsController) {
        this.boidsController = boidsController;
        this.executor = (MyExecutor) Executors.newCachedThreadPool();
    }

    public void runSimulation() {
    	while (running) {
            var t0 = System.currentTimeMillis();
    		var boids = boidsController.getModel().getBoids();

            for (Boid boid : boids) {
               boid.updateVelocity(boidsController.getModel());
            }

            for (Boid boid : boids) {
                boid.updatePos(boidsController.getModel());
            }

            if (boidsController.getView() != null) {
                boidsController.getView().update(framerate);
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
}
