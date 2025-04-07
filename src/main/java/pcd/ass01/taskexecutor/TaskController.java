package pcd.ass01.taskexecutor;

import pcd.ass01.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static pcd.ass01.Constants.TASK_POOL_SIZE;

public class TaskController extends BoidsController {
    private ExecutorService executor;
    private List<List<Boid>> boidsList;
    private final List<Future<Void>> futures;

    public TaskController() {
        super();
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.boidsList = new ArrayList<>();
        this.futures = new ArrayList<>();
    }

    @Override
    public void runSimulation() {
        divideBoids(this.model.getBoids());

        while (running) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            var t0 = System.currentTimeMillis();
            if (!running) {
                break;
            }
            boidsList.forEach(boids -> {
                if (!running) return;
                this.futures.add(this.executor.submit(new UpdateVelocityTask(boids, this.model)));
            });


            this.waitFutures(this.futures);
            this.futures.clear();

            boidsList.forEach(boids -> {
                if (!running) return;
                this.futures.add(this.executor.submit(new UpdatePositionTask(boids, this.model)));
            });

            this.waitFutures(this.futures);
            this.futures.clear();

            updateView(t0);
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

    @Override
    public synchronized void stopSimulation() {
        this.running = false;
        this.paused = false;
        notify();
        stopExecutor();
    }

    private void divideBoids(List<Boid> boids) {
        List<List<Boid>> boidsList = new ArrayList<>();
        int numTask = Math.max(1, boids.size() / TASK_POOL_SIZE);
        int boidsPerThread = boids.size() / numTask;
        int remainingBoids = boids.size() % numTask;

        int startIndex = 0;
        for (int i = 0; i < numTask; i++) {
            int endIndex = startIndex + boidsPerThread + (i < remainingBoids ? 1 : 0);
            boidsList.add(boids.subList(startIndex, endIndex));
            startIndex = endIndex;
        }
        this.boidsList = boidsList;
    }

    private void waitFutures(List<Future<Void>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
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

}
