package pcd.ass01.taskexecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BoidsSimulator {

    private final BoidsController boidsController;
    private int numThreads;
    private ExecutorService executor;
    private volatile boolean running = true;
    private volatile boolean paused = false;
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

        while (this.running) {
            synchronized (this) {
                while (this.paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (!this.running) {
                break;
            }

            CustomCountDownLatch velocityLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                this.executor.execute(new UpdateVelocityTask(velocityLatch, boids, boidsController.getModel()));
            }
            try {
                velocityLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            CustomCountDownLatch positionLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                this.executor.execute(new UpdatePositionTask(positionLatch, boids, boidsController.getModel()));
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

    public void newSimulation() {
        this.running = true;
        new Thread(this::runSimulation).start();
    }

    public synchronized void pauseSimulation() {
        this.paused = true;
    }

    public synchronized void resumeSimulation() {
        this.paused = false;
        notify();
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

    public void stopExecutor() {
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(60, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            this.executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void stopSimulation() {
        this.running = false;
        this.paused = false;
        notify();
    }
}
