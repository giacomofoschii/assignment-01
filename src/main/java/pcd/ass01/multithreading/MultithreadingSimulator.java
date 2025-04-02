package pcd.ass01.multithreading;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.BoidsSimulator;

import java.util.*;
import static java.lang.Thread.currentThread;

public class MultithreadingSimulator extends BoidsSimulator {

    private final BoidsController controller;
    private final Administrator administrator;
    private final CustomCyclicBarrier barrier;
    private final LinkedList<BoidThread> threads;

    private static final int FRAMERATE = 25;
    private int framerate;
    
    public MultithreadingSimulator(BoidsController controller) {
        this.controller = controller;
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CustomCyclicBarrierImpl(numThreads);

        divideBoids();
    }

    public void divideBoids() {
        for (int i = 0; i < numThreads; i++) {
            threads.add(new BoidThread(getThreadPool(i), this.controller, barrier, administrator));
        }
    }

    public void startThreads() {
        for (BoidThread thread : threads) {
            thread.setStopped(false);
            thread.start();
        }
    }
      
    public void runSimulation() {
        while (running) {
            administrator.waitThreads();
    
            updateView();
    
            administrator.signalDone();
        }
    }

    public synchronized void newSimulation() {
        running = true;
        for (int i = 0; i < numThreads; i++) {
            if (!threads.get(i).isAlive()) {
                threads.set(i, new BoidThread(getThreadPool(i), this.controller, barrier, administrator));
                threads.get(i).start();
            } else {
                threads.get(i).assignPool(getThreadPool(i));
                threads.get(i).setStopped(false);
            }
        }
        resumeSimulation();
        new Thread(this::runSimulation).start();
    }

    public synchronized void stopSimulation() {
        paused = false;
        running = false;
        for (BoidThread thread : threads) {
            thread.setStopped(true);
        }
    }

    public void updateView() {
        var t0 = System.currentTimeMillis();

        if (this.controller.getView() != null) {
            this.controller.getView().update(framerate);
            var t1 = System.currentTimeMillis();
            var dtElapsed = t1 - t0;
            var frameRatePeriod = 1000 / FRAMERATE;

            if (dtElapsed < frameRatePeriod) {
                try {
                    Thread.sleep(frameRatePeriod - dtElapsed);
                } catch (InterruptedException ex) {
                    barrier.reset();
                    currentThread().interrupt();
                }
                framerate = FRAMERATE;
            } else {
                framerate = (int) (1000 / dtElapsed);
            }
        }
    }
    

    private List<Boid> getThreadPool(int threadIndex) {
        List<Boid> boids = this.controller.getModel().getBoids();
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }
}
