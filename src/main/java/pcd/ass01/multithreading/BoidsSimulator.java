package pcd.ass01.multithreading;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class BoidsSimulator {

    private final BoidsController controller;
    private final Administrator administrator;
    private final CyclicBarrier barrier;
    private final int numThreads;
    private final Queue<BoidThread> threads;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    
    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(BoidsController controller) {
        this.controller = controller;
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CyclicBarrier(numThreads);

        divideBoids();
    }

    public void divideBoids() {
        List<Boid> boids = controller.getModel().getBoids();
        for (int i = 0; i < numThreads; i++) {
            threads.add(new BoidThread(getThreadPool(i, boids), this.controller, barrier, administrator));
        }
    }
      
    public void runSimulation() {
        for (BoidThread thread : threads) {
            thread.start();
        }
    
        while (running) {

            var t0 = System.currentTimeMillis();
            administrator.waitThreads();
    
            if (this.controller.getView() != null) {
                this.controller.getView().update(framerate);
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
    
            administrator.signalDone();
        }
    }
    

    private List<Boid> getThreadPool(int threadIndex, List<Boid> boids) {
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

    public synchronized void pauseSimulation() {
        this.paused = true;
    }

    public synchronized void resumeSimulation() {
        this.paused = false;
        notifyAll();
    }

    public synchronized void stopSimulation() {
        paused = false;
        running = false;
        for (BoidThread thread : threads) {
            thread.stopThread();
            thread.interrupt();
        }
        threads.clear();
    }

    public boolean isPaused() {
        return this.paused;
    }
}
