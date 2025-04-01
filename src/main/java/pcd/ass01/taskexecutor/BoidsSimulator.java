package pcd.ass01.taskexecutor;

import java.util.concurrent.*;

public class BoidsSimulator {

    private final BoidsController boidsController;
    private final ExecutorService executor;
    private boolean running = true;

    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(final BoidsController boidsController) {
        this.boidsController = boidsController;
        this.executor = Executors.newCachedThreadPool();
    }

    public void runSimulation() {
    	while (running) {
            CustomCountDownLatch velocityLatch = new CustomCountDownLatchImpl(boidsController.getModel().getBoids().size());
            for (Boid boid : boidsController.getModel().getBoids()) {
                executor.submit(new UpdateVelocityTask(velocityLatch, boid, boidsController.getModel()));
            }
            try {
                velocityLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            CustomCountDownLatch positionLatch = new CustomCountDownLatchImpl(boidsController.getModel().getBoids().size());
            for (Boid boid : boidsController.getModel().getBoids()) {
                executor.submit(new UpdatePositionTask(positionLatch, boid, boidsController.getModel()));
            }
            try {
                positionLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            var t0 = System.currentTimeMillis();
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
        executor.shutdown();
    }
}
