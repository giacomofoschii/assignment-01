package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.Administrator;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class JPFBoidsSimulator {

    private final Administrator administrator;
    private final CyclicBarrier barrier;
    private final int numThreads;
    private final Queue<JPFBoidThread> threads;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public JPFBoidsSimulator(JPFBoidsModel model) {
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CyclicBarrier(numThreads);


        List<JPFBoid> boids = model.getBoids();
        for (int i = 0; i < numThreads; i++) {
            threads.add(new JPFBoidThread(getThreadPool(i, boids), barrier, administrator));
        }
    }

    public void runSimulation() {
        for (Thread thread : threads) {
            thread.start();
        }

        while (running) {
            synchronized (this) {  // Aggiunto il blocco synchronized per wait()
                while (paused) {   // Usato while invece di if per evitare spurious wakeups
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Ripristina stato di interruzione
                    }
                }
            }
            administrator.waitThreads();

            administrator.signalDone();
        }
    }


    private List<JPFBoid> getThreadPool(int threadIndex, List<JPFBoid> boids) {
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

    public synchronized void pauseSimulation() {
        paused=true;
    }

    public synchronized void resumeSimulation() {
        paused = false;
        notifyAll(); // Sveglia tutti i thread in attesa su wait()
    }


    public synchronized void stopSimulation() {
        running = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
