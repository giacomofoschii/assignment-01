package pcd.ass01.taskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BoidsSimulator {

    private final BoidsController boidsController;
    private final int numThreads;
    private final ExecutorService executor;
    private boolean running = true;
    private List<List<Boid>> boidsList;

    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(final BoidsController boidsController) {
        this.boidsController = boidsController;
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.boidsList = new ArrayList<>();
    }

    public void runSimulation() {
        divideBoids(boidsController.getModel().getBoids(), numThreads);

        while (running) {
            CustomCountDownLatch velocityLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                executor.submit(new UpdateVelocityTask(velocityLatch, boids, boidsController.getModel()));
            }
            try {
                velocityLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            CustomCountDownLatch positionLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                executor.submit(new UpdatePositionTask(positionLatch, boids, boidsController.getModel()));
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

    public void pauseSimulation() {
        running = false;
    }

    public void resumeSimulation() {
        running = true;
    }

    private void divideBoids(List<Boid> boids, int activeThreads) {
        List<List<Boid>> boidsList = new ArrayList<>();
        int boidsPerThread = boids.size() / activeThreads;
        int remainingBoids = boids.size() % activeThreads;

        int startIndex = 0;
        for (int i = 0; i < activeThreads; i++) {
            int endIndex = startIndex + boidsPerThread + (i < remainingBoids ? 1 : 0);
            boidsList.add(boids.subList(startIndex, endIndex));
            startIndex = endIndex;
        }
        this.boidsList = boidsList;
    }

    public void stopSimulation() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
