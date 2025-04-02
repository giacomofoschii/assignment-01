package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.Administrator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class JPFBoidsController {
    private JPFBoidsModel model;
    private final Administrator administrator;
    private final CyclicBarrier barrier;
    private final int numThreads;
    private final LinkedList<JPFBoidThread> threads;

    public JPFBoidsController() {
        numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.model = new JPFBoidsModel();
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CyclicBarrier(numThreads);
    }

    public JPFBoidsModel getModel() {
        return this.model;
    }

    private void divideBoids() {
        for (int i = 0; i < numThreads; i++) {
            threads.add(new JPFBoidThread(getThreadPool(i), barrier, administrator));
        }
    }

    private void startThreads() {
        for (JPFBoidThread thread : threads) {
            thread.start();
        }
    }

    public void runSimulation() {
        divideBoids();
        startThreads();
        administrator.waitThreads();
        administrator.signalDone();
    }

    public synchronized void stopSimulation() {
        for (JPFBoidThread thread : threads) {
            thread.interrupt();
        }
        for (JPFBoidThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void newSimulation() {
        for (int i = 0; i < numThreads; i++) {
            if (!threads.get(i).isAlive()) {
                threads.set(i, new JPFBoidThread(getThreadPool(i), barrier, administrator));
                threads.get(i).start();
            } else {
                threads.get(i).assignPool(getThreadPool(i));
            }
        }
        runSimulation();
    }

    private List<JPFBoid> getThreadPool(int threadIndex) {
        List<JPFBoid> boids = model.getBoids();
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

}
