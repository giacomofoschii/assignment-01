package pcd.ass01.taskexecutor;

import pcd.ass01.*;
import pcd.ass01.utils.CustomCountDownLatch;
import pcd.ass01.utils.CustomCountDownLatchImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskController extends BoidsController {
    private ExecutorService executor;
    private List<List<Boid>> boidsList;

    public TaskController() {
        super();
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    public void runSimulation() {
        divideBoids(model.getBoids(), numThreads);

        while (running) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            if (!running) {
                break;
            }

            CustomCountDownLatch velocityLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                this.executor.execute(new UpdateVelocityTask(velocityLatch, boids, model));
            }

            updateView();

            CustomCountDownLatch positionLatch = new CustomCountDownLatchImpl(boidsList.size());
            for (List<Boid> boids : boidsList) {
                this.executor.execute(new UpdatePositionTask(positionLatch, boids, model));
            }
        }
    }

    @Override
    public void newSimulation() {
        this.running = true;
        this.executor = Executors.newFixedThreadPool(numThreads);
        new Thread(this::runSimulation).start();
    }

    @Override
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
            if (!this.executor.awaitTermination(3, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            this.executor.shutdownNow();
        }
    }

    @Override
    public synchronized void stopSimulation() {
        this.running = false;
        this.paused = false;
        notify();
        stopExecutor();
    }
}
