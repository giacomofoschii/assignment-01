package pcd.ass01.multithreading;

import pcd.ass01.*;
import pcd.ass01.utils.CustomCyclicBarrier;
import pcd.ass01.utils.CustomCyclicBarrierImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultithreadingController extends BoidsController {

    private final MultiAdministrator multiAdministrator;
    private final CustomCyclicBarrier barrier;
    private final LinkedList<BoidThread> threads;

    public MultithreadingController() {
        super();
        this.threads = new LinkedList<>();
        this.multiAdministrator = new MultiAdministrator(numThreads);
        this.barrier = new CustomCyclicBarrierImpl(numThreads);

        divideBoids();
    }

    public void divideBoids() {
        for (int i = 0; i < numThreads; i++) {
            threads.add(new BoidThread(getThreadPool(i), this, barrier, multiAdministrator));
        }
    }

    public void startThreads() {
        for (BoidThread thread : threads) {
            thread.setStopped(false);
            thread.start();
        }
    }

    @Override
    public void runSimulation() {
        while (running) {
            multiAdministrator.waitThreads();

            updateView();

            multiAdministrator.signalDone();
        }
    }

    @Override
    public synchronized void resumeSimulation() {
        this.paused = false;
        notifyAll();
    }

    @Override
    public synchronized void newSimulation() {
        running = true;
        for (int i = 0; i < numThreads; i++) {
            if (!threads.get(i).isAlive()) {
                threads.set(i, new BoidThread(getThreadPool(i), this, barrier, multiAdministrator));
                threads.get(i).start();
            } else {
                threads.get(i).assignPool(getThreadPool(i));
                threads.get(i).setStopped(false);
            }
        }
        resumeSimulation();
        new Thread(this::runSimulation).start();
    }

    @Override
    public synchronized void stopSimulation() {
        paused = false;
        running = false;
        threads.forEach(thread -> thread.setStopped(true));
    }

    public boolean isPaused() {
        return paused;
    }

    private List<Boid> getThreadPool(int threadIndex) {
        List<Boid> boids = model.getBoids();
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

}