package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.Administrator;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class JPFBoidsSimulator {

    private final Administrator administrator;
    private final CyclicBarrier barrier;
    private final int numThreads;
    private final JPFBoidsController controller;
    private final LinkedList<JPFBoidThread> threads;
    private volatile boolean running = true;

    public JPFBoidsSimulator(JPFBoidsController controller) {
        this.controller = controller;
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CyclicBarrier(numThreads);

        divideBoids();
    }

    public void divideBoids() {
        for (int i = 0; i < numThreads; i++) {
            threads.add(new JPFBoidThread(getThreadPool(i), barrier, administrator));
        }
    }

    public void startThreads() {
        for (JPFBoidThread thread : threads) {
            thread.setStopped(false);
            thread.start();
        }
    }

    public void runSimulation() {
        int i = 20;
        while (running) {
            administrator.waitThreads();
            administrator.signalDone();
            if(i>0) {
                i--;
            } else {
                stopSimulation();
            }
        }
    }

    public synchronized void stopSimulation() {
        running = false;
        for (JPFBoidThread thread : threads) {
            thread.setStopped(true);
        }
    }

    public synchronized void newSimulation() {
        running = true;
        for (int i = 0; i < numThreads; i++) {
            if (!threads.get(i).isAlive()) {
                threads.set(i, new JPFBoidThread(getThreadPool(i), barrier, administrator));
                threads.get(i).start();
            } else {
                threads.get(i).assignPool(getThreadPool(i));
                threads.get(i).setStopped(false);
            }
        }
        runSimulation();
    }

    private List<JPFBoid> getThreadPool(int threadIndex) {
        List<JPFBoid> boids = this.controller.getModel().getBoids();
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }
}
